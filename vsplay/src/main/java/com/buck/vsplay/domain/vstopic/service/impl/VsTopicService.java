package com.buck.vsplay.domain.vstopic.service.impl;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.statistics.event.TopicEvent;
import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.mapper.TournamentMapper;
import com.buck.vsplay.domain.vstopic.mapper.VsTopicMapper;
import com.buck.vsplay.domain.vstopic.moderation.TopicAccessGuard;
import com.buck.vsplay.domain.vstopic.repository.TournamentRepository;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.domain.vstopic.service.IVsTopicService;
import com.buck.vsplay.global.constants.ModerationStatus;
import com.buck.vsplay.global.constants.SortBy;
import com.buck.vsplay.global.constants.Visibility;
import com.buck.vsplay.global.dto.Pagination;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import com.buck.vsplay.global.util.gpt.client.BadWordFilter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VsTopicService implements IVsTopicService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final VsTopicRepository vsTopicRepository;
    private final TournamentRepository tournamentRepository;
    private final VsTopicMapper vsTopicMapper;
    private final TournamentMapper tournamentMapper;
    private final AuthUserService authUserService;
    private final EntityManager entityManager;
    private final BadWordFilter badWordFilter;
    private final S3Util s3Util;

    @Override
    public VsTopicDto.VsTopicCreateResponse createVsTopic(VsTopicDto.VsTopicCreateRequest createVsTopicRequest) {
        Member existMember = authUserService.getAuthUser();

        if(isMissingPasswordForProtectedTopic(createVsTopicRequest.getVisibility(), createVsTopicRequest.getAccessCode())){
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_PASSWORD_REQUIRE);
        }

        List<String> stringList = buildStringList(
                createVsTopicRequest.getTitle(),
                createVsTopicRequest.getSubject(),
                createVsTopicRequest.getDescription());

        boolean hasBadWord = badWordFilter.containsBadWords(stringList);

        if( hasBadWord ){
            throw new VsTopicException(VsTopicExceptionCode.BAD_WORD_DETECTED);
        }

        VsTopic vsTopic = vsTopicMapper.toEntityFromVstopicCreateRequestDtoWithoutThumbnail(createVsTopicRequest);
        vsTopic.setMember(existMember);
        vsTopic.setModerationStatus(ModerationStatus.PASSED);
        vsTopic.setThumbnail(createVsTopicRequest.getThumbnail());

        entityManager.persist(vsTopic);
        entityManager.flush();

        applicationEventPublisher.publishEvent(new TopicEvent.CreateEvent(vsTopic));

        return VsTopicDto.VsTopicCreateResponse.builder()
                .title(vsTopic.getTitle())
                .topicId(vsTopic.getId())
                .subject(vsTopic.getSubject())
                .description(vsTopic.getDescription())
                .visibility(vsTopic.getVisibility())
                .build();
    }

    @Override
    public void updateVsTopic(Long topicId, VsTopicDto.VsTopicUpdateRequest updateVsTopicRequest) {
        Member existMember = authUserService.getAuthUser();

        VsTopic vsTopic = vsTopicRepository.findByIdAndDeletedFalse(topicId).orElseThrow(
                () -> new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));

        if(!vsTopic.getMember().getId().equals(existMember.getId())){
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_CREATOR_ONLY);
        }


        if(isMissingPasswordForProtectedTopic(updateVsTopicRequest.getVisibility(), updateVsTopicRequest.getAccessCode())){
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_PASSWORD_REQUIRE);
        }

        List<String> stringList = buildStringList(
                updateVsTopicRequest.getTitle(),
                updateVsTopicRequest.getSubject(),
                updateVsTopicRequest.getDescription());

        boolean hasBadWord = badWordFilter.containsBadWords(stringList);

        if( hasBadWord ){
            throw new VsTopicException(VsTopicExceptionCode.BAD_WORD_DETECTED);
        }

        vsTopic.setModerationStatus(ModerationStatus.PASSED);

        if(isThumbnailUpdated(updateVsTopicRequest.getThumbnail())){ // 썸네일 존재 시 업데이트
            vsTopic.setThumbnail(updateVsTopicRequest.getThumbnail());
        }

        vsTopicMapper.toUpdateVsTopicFromUpdateRequest(updateVsTopicRequest, vsTopic);
        vsTopicRepository.save(vsTopic);

    }

    @Override
    public VsTopicDto.VsTopicDetailWithTournamentsResponse getVsTopicDetailWithTournaments(Long topicId) {
        Optional<Member> authUser = authUserService.getAuthUserOptional();

        VsTopic vsTopic = vsTopicRepository.findByIdAndDeletedFalse(topicId).orElseThrow(() ->
                new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));

        TopicAccessGuard.validateTopicAccess(vsTopic, authUser.orElse(null));

        List<VsTopicDto.Tournament> tournamentList = new ArrayList<>();
        List<TopicTournament> topicTournaments = tournamentRepository.findByVsTopicIdAndActiveTrue(vsTopic.getId());

        if ( topicTournaments != null && !topicTournaments.isEmpty() ) {
            for (TopicTournament tournament : topicTournaments) {
                tournamentList.add(tournamentMapper.toTournamentDtoFromEntityWithoutId(tournament));
            }
        }

        return VsTopicDto.VsTopicDetailWithTournamentsResponse.builder()
                .topic(vsTopicMapper.toVsTopicDtoFromEntityWithModeration(vsTopic, s3Util))
                .tournamentList(tournamentList)
                .build();
    }

    @Override
    public VsTopicDto.VsTopicSearchResponse searchPublicVsTopic( VsTopicDto.VsTopicSearchRequest vsTopicSearchRequest) {
        int page = Math.max(vsTopicSearchRequest.getPage() - 1 , 0); // index 조정

        SortBy searchSortBy = vsTopicSearchRequest.getSearchSortBy();
        Page<VsTopic> topicsWithPage;

        if( SortBy.LATEST == searchSortBy) { // 인기순 정렬
            topicsWithPage = vsTopicRepository.findPublicTopicsByTitleOrderByNewest(vsTopicSearchRequest.getKeyword(), PageRequest.of(page, vsTopicSearchRequest.getSize()));
        } else { // 최신순 정렬
            topicsWithPage = vsTopicRepository.findPublicTopicsByTitleOrderByTotalMatches(vsTopicSearchRequest.getKeyword(), PageRequest.of(page, vsTopicSearchRequest.getSize()));
        }

        return VsTopicDto.VsTopicSearchResponse.builder()
                .topicList(vsTopicMapper.toVsTopicDtoWithThumbnailListFromEntityList(topicsWithPage.getContent(), s3Util))
                .pagination(Pagination.builder()
                        .totalPages(topicsWithPage.getTotalPages())
                        .totalItems(topicsWithPage.getTotalElements())
                        .currentPage(topicsWithPage.getNumber() + 1) // index 조정
                        .pageSize(topicsWithPage.getSize())
                        .build())
                .build();
    }

    @Override
    public List<VsTopicDto.TopicVisibility> getTopicVisibilities() {

        List<VsTopicDto.TopicVisibility> topicVisibilityList = new ArrayList<>();

        for (Visibility visibility : Visibility.values()) {
            topicVisibilityList.add(
                    VsTopicDto.TopicVisibility.builder()
                            .visibility(visibility.getCode())
                            .description(visibility.getDescription())
                            .build());
        }

        return topicVisibilityList;
    }

    @Override
    public VsTopicDto.MyTopicsResponse getMyVsTopics(VsTopicDto.VsTopicSearchRequest vsTopicSearchRequest) {
        Member member = authUserService.getAuthUser();

        int page = Math.max(vsTopicSearchRequest.getPage() - 1 , 0); // index 조정
        Page<VsTopic> topicsWithPage;

        topicsWithPage = vsTopicRepository.findTopicsByMemberIdAndTitleAndVisibility(member.getId(), vsTopicSearchRequest.getKeyword(), vsTopicSearchRequest.getVisibility(), PageRequest.of(page, vsTopicSearchRequest.getSize()));

        return VsTopicDto.MyTopicsResponse.builder()
                .topicList(vsTopicMapper.toVsTopicDtoWithModerationListFromEntityList(topicsWithPage.getContent(), s3Util))
                .pagination(Pagination.builder()
                        .totalPages(topicsWithPage.getTotalPages())
                        .totalItems(topicsWithPage.getTotalElements())
                        .currentPage(topicsWithPage.getNumber() + 1) // index 조정
                        .pageSize(topicsWithPage.getSize())
                        .build())
                .build();
    }

    private List<String> buildStringList(String ... strings){
        return List.of(strings);
    }

    private boolean isThumbnailUpdated(String thumbnail){
        return thumbnail != null && !thumbnail.isEmpty();
    }

    private boolean isMissingPasswordForProtectedTopic(Visibility visibility, String accessCode){
        return visibility == Visibility.PASSWORD && (accessCode == null || accessCode.isBlank());
    }
}

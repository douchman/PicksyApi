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
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.domain.vstopic.service.IVsTopicService;
import com.buck.vsplay.global.constants.SortBy;
import com.buck.vsplay.global.constants.Visibility;
import com.buck.vsplay.global.dto.Pagination;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import com.buck.vsplay.global.util.aws.s3.dto.S3Dto;
import com.buck.vsplay.global.util.gpt.client.BadWordFilter;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VsTopicService implements IVsTopicService {

    @Value("${app.base-domain}")
    private String appBaseDomain;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final S3Util s3Util;
    private final VsTopicRepository vsTopicRepository;
    private final VsTopicMapper vsTopicMapper;
    private final TournamentMapper tournamentMapper;
    private final AuthUserService authUserService;
    private final EntityManager entityManager;
    private final BadWordFilter badWordFilter;

    @Override
    public VsTopicDto.VsTopicCreateResponse createVsTopic(VsTopicDto.VsTopicCreateRequest createVsTopicRequest) {
        Member existMember = authUserService.getAuthUser();
        S3Dto.S3UploadResult s3UploadResult = s3Util.putObject(createVsTopicRequest.getThumbnail() , existMember.getId().toString());

        List<String> stringList = buildStringList(
                createVsTopicRequest.getTitle(),
                createVsTopicRequest.getSubject(),
                createVsTopicRequest.getDescription());


        boolean hasBadWord = badWordFilter.containsBadWords(stringList);

        if( hasBadWord ){
            throw new VsTopicException(VsTopicExceptionCode.BAD_WORD_DETECTED);
        }

        Visibility requestVisibility = createVsTopicRequest.getVisibility();
        VsTopic vsTopic = vsTopicMapper.toEntityFromVstopicCreateRequestDtoWithoutThumbnail(createVsTopicRequest);
        vsTopic.setMember(existMember);
        vsTopic.setThumbnail(s3UploadResult.getObjectKey());

        entityManager.persist(vsTopic);
        entityManager.flush();

        vsTopic.setShortCode(Visibility.UNLISTED.equals(requestVisibility) ? generateShortCode(vsTopic.getId()) : null);

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

        List<String> stringList = buildStringList(
                updateVsTopicRequest.getTitle(),
                updateVsTopicRequest.getSubject(),
                updateVsTopicRequest.getDescription());

        boolean hasBadWord = badWordFilter.containsBadWords(stringList);

        if( hasBadWord ){
            throw new VsTopicException(VsTopicExceptionCode.BAD_WORD_DETECTED);
        }

        MultipartFile thumbnail = updateVsTopicRequest.getThumbnail();
        boolean isFileExist = (thumbnail != null && !thumbnail.isEmpty());
        Visibility updateVisibility = updateVsTopicRequest.getVisibility();

        if (isFileExist) {
            String objectKey = s3Util.buildS3Path(existMember.getId().toString(), String.valueOf(topicId));
            S3Dto.S3UploadResult s3UploadResult = s3Util.putObject(thumbnail, objectKey);
            vsTopic.setThumbnail(s3UploadResult.getObjectKey());
        }

        vsTopic.setShortCode(Visibility.UNLISTED.equals(updateVisibility) ? generateShortCode(vsTopic.getId()) : null);

        vsTopicMapper.updateVsTopicFromUpdateRequest(updateVsTopicRequest, vsTopic);
        vsTopicRepository.save(vsTopic);

    }

    @Override
    public VsTopicDto.VsTopicDetailWithTournamentsResponse getVsTopicDetailWithTournaments(Long topicId) {
        Optional<Member> authUser = authUserService.getAuthUserOptional();
        VsTopicDto.VsTopicDetailWithTournamentsResponse topicDetailWithTournamentsResponse = new VsTopicDto.VsTopicDetailWithTournamentsResponse();

        VsTopic vsTopic = vsTopicRepository.findWithTournamentsByTopicId(topicId);

        TopicAccessGuard.validateTopicAccess(vsTopic, authUser.orElse(null));

        topicDetailWithTournamentsResponse.setTopic(vsTopicMapper.toVsTopicDtoFromEntityWithThumbnail(vsTopic));

        if ( vsTopic.getTournaments() != null && !vsTopic.getTournaments().isEmpty() ) {
            for (TopicTournament tournament : vsTopic.getTournaments()) {
                topicDetailWithTournamentsResponse.getTournamentList()
                        .add(tournamentMapper.toTournamentDtoFromEntityWithoutId(tournament));
            }
        }

        return topicDetailWithTournamentsResponse;
    }

    @Override
    public VsTopicDto.VsTopicDetailWithTournamentsResponse getVsTopicDetailWithTournamentsByShortCode(String shortCode) {

        VsTopic vsTopic = vsTopicRepository.findWithTournamentsByShortCode(shortCode);

        if(vsTopic == null) {
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND);
        }

        VsTopicDto.VsTopicDetailWithTournamentsResponse topicDetailWithTournamentsResponse = new VsTopicDto.VsTopicDetailWithTournamentsResponse();

        topicDetailWithTournamentsResponse.setTopic(vsTopicMapper.toVsTopicDtoFromEntityWithThumbnail(vsTopic));

        if ( vsTopic.getTournaments() != null && !vsTopic.getTournaments().isEmpty() ) {
            for (TopicTournament tournament : vsTopic.getTournaments()) {
                topicDetailWithTournamentsResponse.getTournamentList()
                        .add(tournamentMapper.toTournamentDtoFromEntityWithoutId(tournament));
            }
        }
        return topicDetailWithTournamentsResponse;
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
                .topicList(vsTopicMapper.toVsTopicDtoWithThumbnailListFromEntityList(topicsWithPage.getContent()))
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
    public VsTopicDto.VsTopicSearchResponse getMyVsTopics(VsTopicDto.VsTopicSearchRequest vsTopicSearchRequest) {
        Member member = authUserService.getAuthUser();

        int page = Math.max(vsTopicSearchRequest.getPage() - 1 , 0); // index 조정
        Page<VsTopic> topicsWithPage;

        topicsWithPage = vsTopicRepository.findTopicsByMemberIdAndTitleAndVisibility(member.getId(), vsTopicSearchRequest.getKeyword(), vsTopicSearchRequest.getVisibility(), PageRequest.of(page, vsTopicSearchRequest.getSize()));

        return VsTopicDto.VsTopicSearchResponse.builder()
                .topicList(vsTopicMapper.toVsTopicDtoWithThumbnailListFromEntityList(topicsWithPage.getContent()))
                .pagination(Pagination.builder()
                        .totalPages(topicsWithPage.getTotalPages())
                        .totalItems(topicsWithPage.getTotalElements())
                        .currentPage(topicsWithPage.getNumber() + 1) // index 조정
                        .pageSize(topicsWithPage.getSize())
                        .build())
                .build();
    }

    @Override
    public VsTopicDto.VsTopicUnlistedLinkResponse getVsTopicUnlistedLink(Long topicId) {
        VsTopic vsTopic = vsTopicRepository.findByIdAndDeletedFalse(topicId).orElseThrow(
                () -> new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));

        if(!isUnlistedTopic(vsTopic.getVisibility())){
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_UNLISTED);
        }

        return VsTopicDto.VsTopicUnlistedLinkResponse.builder()
                .link(generateFullUrlOfUnlistedVstopic(vsTopic.getShortCode()))
                .build();
    }

    private String generateShortCode(Long topicId) {
        UUID uuid = UUID.nameUUIDFromBytes(String.valueOf(topicId).getBytes(StandardCharsets.UTF_8));
        return uuid.toString().replace("-", "").substring(0, 32);
    }

    private boolean isUnlistedTopic(Visibility visibility) {
        return Visibility.UNLISTED.equals(visibility);
    }

    private String generateFullUrlOfUnlistedVstopic(String shortCode){
        return appBaseDomain + "vstopic/link/" + shortCode;
    }

    private List<String> buildStringList(String ... strings){
        return List.of(strings);
    }
}

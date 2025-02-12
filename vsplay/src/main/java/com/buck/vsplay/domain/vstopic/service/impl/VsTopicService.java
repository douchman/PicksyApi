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
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.domain.vstopic.service.IVsTopicService;
import com.buck.vsplay.global.constants.Visibility;
import com.buck.vsplay.global.dto.Pagination;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import com.buck.vsplay.global.util.aws.s3.dto.S3Dto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VsTopicService implements IVsTopicService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final S3Util s3Util;
    private final VsTopicRepository vsTopicRepository;
    private final VsTopicMapper vsTopicMapper;
    private final TournamentMapper tournamentMapper;
    private final AuthUserService authUserService;

    @Override
    public void createVsTopic(VsTopicDto.VsTopicCreateRequest createVsTopicRequest) {
        Member existMember = authUserService.getAuthUser();
        S3Dto.S3UploadResult s3UploadResult = s3Util.putObject(createVsTopicRequest.getThumbnail() , existMember.getId().toString());

        VsTopic vsTopic = vsTopicMapper.toEntityFromVstopicCreateRequestDtoWithoutThumbnail(createVsTopicRequest);
        vsTopic.setMember(existMember);
        vsTopic.setThumbnail(s3UploadResult.getObjectKey());

        vsTopicRepository.save(vsTopic);
        applicationEventPublisher.publishEvent(new TopicEvent.CreateEvent(vsTopic));
    }

    @Override
    public void updateVsTopic(Long topicId, VsTopicDto.VsTopicUpdateRequest updateVsTopicRequest) {
        Member existMember = authUserService.getAuthUser();
        MultipartFile thumbnail = updateVsTopicRequest.getThumbnail();
        boolean isFileExist = (thumbnail != null && !thumbnail.isEmpty());

        VsTopic vsTopic = vsTopicRepository.findById(topicId).orElseThrow(
                () -> new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));

        if (isFileExist) {
            String objectKey = s3Util.buildS3Path(existMember.getId().toString(), String.valueOf(topicId));
            S3Dto.S3UploadResult s3UploadResult = s3Util.putObject(thumbnail, objectKey);
            vsTopic.setThumbnail(s3UploadResult.getObjectKey());
        }
        vsTopicMapper.updateVsTopicFromUpdateRequest(updateVsTopicRequest, vsTopic);
        vsTopicRepository.save(vsTopic);

    }

    @Override
    public VsTopicDto.VsTopicDetailWithTournamentsResponse getVsTopicDetailWithTournaments(Long topicId) {

        VsTopicDto.VsTopicDetailWithTournamentsResponse topicDetailWithTournamentsResponse = new VsTopicDto.VsTopicDetailWithTournamentsResponse();

        VsTopic vsTopic = vsTopicRepository.findWithTournamentsByTopicId(topicId);

        if ( vsTopic == null ) {
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND);
        }

        topicDetailWithTournamentsResponse.setTopic(vsTopicMapper.toVsTopicDtoFromEntity(vsTopic));

        if ( vsTopic.getTournaments() != null && !vsTopic.getTournaments().isEmpty() ) {
            for (TopicTournament tournament : vsTopic.getTournaments()) {
                topicDetailWithTournamentsResponse.getTournamentList()
                        .add(tournamentMapper.toTournamentDtoFromEntityWithoutId(tournament));
            }
        }

        return topicDetailWithTournamentsResponse;
    }

    @Override
    public VsTopicDto.VsTopicSearchResponse getPublicVsTopicList( VsTopicDto.VsTopicSearchRequest vsTopicSearchRequest) {
        int page = Math.max(vsTopicSearchRequest.getPage() - 1 , 0); // index 조정

        Page<VsTopic> topicPage = vsTopicRepository.findByTitleContainingAndSubjectContaining(
                vsTopicSearchRequest.getKeyword(), // 제목 : title
                vsTopicSearchRequest.getKeyword(), // 주제 : subject
                PageRequest.of(page, vsTopicSearchRequest.getSize(), Sort.by(Sort.Direction.DESC, "createdAt")));

        return VsTopicDto.VsTopicSearchResponse.builder()
                .topicList(vsTopicMapper.toVsTopicDtoWithThumbnailListFromEntityList(topicPage.getContent()))
                .pagination(Pagination.builder()
                        .totalPages(topicPage.getTotalPages())
                        .totalItems(topicPage.getTotalElements())
                        .currentPage(topicPage.getNumber() + 1) // index 조정
                        .pageSize(topicPage.getSize())
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
}

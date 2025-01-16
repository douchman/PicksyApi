package com.buck.vsplay.domain.vstopic.service.impl;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.mapper.VsTopicMapper;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.domain.vstopic.service.IVsTopicService;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import com.buck.vsplay.global.util.aws.s3.dto.S3Dto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VsTopicService implements IVsTopicService {
    private final S3Util s3Util;
    private final VsTopicRepository vsTopicRepository;
    private final VsTopicMapper vsTopicMapper;
    private final AuthUserService authUserService;

    @Override
    public void createVsTopic(VsTopicDto.VsTopicCreateRequest createVsTopicRequest) {
        Member existMember = authUserService.getAuthUser();
        S3Dto.S3UploadResult s3UploadResult = s3Util.putObject(createVsTopicRequest.getThumbnail() , existMember.getId().toString());

        VsTopic vsTopic = vsTopicMapper.toEntity(createVsTopicRequest);
        vsTopic.setMember(existMember);
        vsTopic.setThumbnail(s3UploadResult.getObjectKey());

        vsTopicRepository.save(vsTopic);
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
}

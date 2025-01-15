package com.buck.vsplay.domain.vstopic.service.impl;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
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
}

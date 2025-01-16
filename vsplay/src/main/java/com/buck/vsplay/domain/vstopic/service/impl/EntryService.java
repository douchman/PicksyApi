package com.buck.vsplay.domain.vstopic.service.impl;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.mapper.TopicEntryMapper;
import com.buck.vsplay.domain.vstopic.repository.EntryRepository;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.domain.vstopic.service.IEntryService;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import com.buck.vsplay.global.util.aws.s3.dto.S3Dto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EntryService implements IEntryService {

    private final S3Util s3Util;
    private final VsTopicRepository topicRepository;
    private final TopicEntryMapper topicEntryMapper;
    private final AuthUserService authUserService;
    private final EntryRepository entryRepository;

    @Override
    public void createEntries(EntryDto.CreateEntriesRequest request) {
        Member authUser = authUserService.getAuthUser();
        Long topicId = request.getTopicId();
        VsTopic vsTopic = topicRepository.findById(topicId).orElseThrow(() ->
                new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));


        List<EntryDto.Entry> entries = request.getEntries();
        List<TopicEntry> topicEntries = new ArrayList<>();
        String objectPath = s3Util.buildS3Path(String.valueOf(authUser.getId()), String.valueOf(vsTopic.getId()));

        for (EntryDto.Entry entry : entries) {
            S3Dto.S3UploadResult s3UploadResult = s3Util.putObject(entry.getFile(), objectPath);
            TopicEntry topicEntry = topicEntryMapper.toTopicEntryWithTopic(entry, vsTopic);
            topicEntry.setMediaUrl(s3UploadResult.getObjectKey());
            topicEntry.setMediaType(s3UploadResult.getMediaType());
            topicEntries.add(topicEntry); // DTO -> Entity 매핑
        }

        entryRepository.saveAll(topicEntries);

    }
}

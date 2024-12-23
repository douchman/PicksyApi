package com.buck.vsplay.domain.vstopic.service.impl;

import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.mapper.VsTopicMapper;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.domain.vstopic.service.IVsTopicService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VsTopicService implements IVsTopicService {
    private final VsTopicRepository vsTopicRepository;
    private final VsTopicMapper vsTopicMapper;

    @Override
    public void createVsTopic(VsTopicDto.VsTopicCreateRequest createVsTopicRequest) {
        log.info("created VsTopic Request -> {}", createVsTopicRequest);
        vsTopicRepository.save(vsTopicMapper.toEntity(createVsTopicRequest));
    }
}

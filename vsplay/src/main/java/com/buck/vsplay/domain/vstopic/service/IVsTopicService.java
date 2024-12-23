package com.buck.vsplay.domain.vstopic.service;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;

public interface IVsTopicService {
    void createVsTopic(VsTopicDto.VsTopicCreateRequest createVsTopicRequest);
}

package com.buck.vsplay.domain.vstopic.service;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;

public interface IVsTopicService {
    void createVsTopic(VsTopicDto.VsTopicCreateRequest createVsTopicRequest);
    void updateVsTopic(Long topicId, VsTopicDto.VsTopicUpdateRequest updateVsTopicRequest);
    VsTopicDto.VsTopicDetailWithTournamentsResponse getVsTopicDetailWithTournaments(Long topicId);
}

package com.buck.vsplay.domain.vstopic.service;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;

import java.util.List;

public interface IVsTopicService {
    VsTopicDto.VsTopicCreateResponse createVsTopic(VsTopicDto.VsTopicCreateRequest createVsTopicRequest);
    void updateVsTopic(Long topicId, VsTopicDto.VsTopicUpdateRequest updateVsTopicRequest);
    VsTopicDto.VsTopicDetailResponse getVsTopicDetailForModify(Long topicId);
    VsTopicDto.VsTopicDetailWithTournamentsResponse getVsTopicDetailWithTournaments(Long topicId);
    VsTopicDto.VsTopicDetailWithTournamentsResponse getVsTopicDetailWithTournamentsByShortCode(String shortCode);
    VsTopicDto.VsTopicSearchResponse searchPublicVsTopic(VsTopicDto.VsTopicSearchRequest vsTopicSearchRequest);
    List<VsTopicDto.TopicVisibility> getTopicVisibilities();
    VsTopicDto.VsTopicSearchResponse getMyVsTopics(VsTopicDto.VsTopicSearchRequest vsTopicSearchRequest);
    VsTopicDto.VsTopicUnlistedLinkResponse getVsTopicUnlistedLink(Long topicId);
}

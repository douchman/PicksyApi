package com.buck.vsplay.domain.vstopic.service;


import com.buck.vsplay.domain.vstopic.dto.TopicPlayRecordDto;

public interface IMatchService {

    TopicPlayRecordDto.PlayRecordResponse createTopicPlayRecord(Long topicId, TopicPlayRecordDto.PlayRecordRequest playRecordRequest);
}

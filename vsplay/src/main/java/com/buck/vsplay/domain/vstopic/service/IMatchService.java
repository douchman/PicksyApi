package com.buck.vsplay.domain.vstopic.service;


import com.buck.vsplay.domain.vstopic.dto.EntryMatchDto;
import com.buck.vsplay.domain.vstopic.dto.TopicPlayRecordDto;

public interface IMatchService {

    TopicPlayRecordDto.PlayRecordResponse createTopicPlayRecord(Long topicId, TopicPlayRecordDto.PlayRecordRequest playRecordRequest);
    EntryMatchDto.EntryMatchResponse getEntryMatch(Long playRecordId);
    EntryMatchDto.UpdateEntryMatchResultResponse updateEntryMatchResult (Long playRecordId, Long matchId, EntryMatchDto.EntryMatchResultRequest entryMatchResultRequest);
}

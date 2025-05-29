package com.buck.vsplay.domain.statistics.service;

import com.buck.vsplay.domain.statistics.dto.EntryStatisticsDto;
import com.buck.vsplay.domain.vstopic.entity.EntryMatch;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;

import java.util.List;

public interface IEntryStatisticsService {
    void createEntryStatistics(List<TopicEntry> topicEntryList);
    void recordEntryMatchStats(EntryMatch entryMatch);
    EntryStatisticsDto.EntryStatSearchResponse getEntryStatisticsWithEntryInfo(Long topicId, EntryStatisticsDto.EntryStatSearchRequest entryStatSearchRequest);
    EntryStatisticsDto.SingleEntryStatsResponse getSingleEntryStatistics(Long topicId, Long entryId);
}

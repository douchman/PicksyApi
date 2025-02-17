package com.buck.vsplay.domain.statistics.service;

import com.buck.vsplay.domain.statistics.dto.EntryVersusStatisticsDto;
import com.buck.vsplay.domain.statistics.event.EntryEvent;
import com.buck.vsplay.domain.vstopic.entity.EntryMatch;

import java.util.List;

public interface IEntryVersusStatisticsService {
    void handleEntryMatchCompletedEventForVersusStats(EntryEvent.VersusStatisticsEvent matchCompleteEvent);
    void upsertEntryVersusStatistics(EntryMatch entryMatch);

    List<EntryVersusStatisticsDto.EntryVersusStatistics> getEntryVersusStatistics(Long topicId, Long entryId);
}

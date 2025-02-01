package com.buck.vsplay.domain.statistics.service;

import com.buck.vsplay.domain.statistics.event.EntryEvent;
import com.buck.vsplay.domain.vstopic.entity.EntryMatch;

public interface IEntryVersusStatisticsService {
    void handleEntryMatchCompletedEventForVersusStats(EntryEvent.VersusStatisticsEvent matchCompleteEvent);
    void upsertEntryVersusStatistics(EntryMatch entryMatch);
}

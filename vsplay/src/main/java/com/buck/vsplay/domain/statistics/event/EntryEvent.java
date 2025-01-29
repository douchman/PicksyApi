package com.buck.vsplay.domain.statistics.event;

import com.buck.vsplay.domain.vstopic.entity.EntryMatch;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryEvent {

    @Data
    @AllArgsConstructor
    public static class CreateEvent{
        List<TopicEntry> topicEntryList;
    }

    @Data
    @AllArgsConstructor
    public static class MatchCompleteEvent{
        EntryMatch entryMatch;
    }
}

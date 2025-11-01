package com.buck.vsplay.domain.entry.service.impl;

import com.buck.vsplay.domain.entry.dto.EntryDto;

import java.util.List;

public interface IEntryCache {
    List<EntryDto.Entry> getCachedEntries(Long topicId);
    void putEntries(Long topicId, List<EntryDto.Entry> entries);
    void evictEntries(Long topicId);
}

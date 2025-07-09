package com.buck.vsplay.domain.vstopic.service;

import com.buck.vsplay.domain.entry.dto.EntryDto;

public interface IEntryService {
    EntryDto.EntryList getEntriesByTopicId(Long topicId);
    void createEntries(Long topicId, EntryDto.CreateEntriesRequest request);
    void updateEntries(Long topicId, EntryDto.UpdateEntryRequest updatedRequest);
}

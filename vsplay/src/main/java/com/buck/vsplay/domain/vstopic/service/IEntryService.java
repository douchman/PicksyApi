package com.buck.vsplay.domain.vstopic.service;

import com.buck.vsplay.domain.vstopic.dto.EntryDto;

public interface IEntryService {
    EntryDto.CreatedEntryList getEntriesByTopicId(Long topicId);
    void createEntries(Long topicId, EntryDto.CreateEntriesRequest request);
}

package com.buck.vsplay.domain.vstopic.service;

import com.buck.vsplay.domain.vstopic.dto.EntryDto;

public interface IEntryService {
    void createEntries(EntryDto.createEntriesRequest request);
}

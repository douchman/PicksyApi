package com.buck.vsplay.domain.vstopic.controller;


import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.service.impl.EntryService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;

    @GetMapping("vstopic/entries/{id}")
    public ResponseEntity<SingleResponseDto<EntryDto.CreatedEntryList>> getAllEntries(
            @PathVariable("id") Long topicId
    ) {

        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), entryService.getEntriesByTopicId(topicId)) ,HttpStatus.OK);
    }

    @PostMapping("vstopic/{id}/entries")
    public ResponseEntity<SingleResponseDto<Integer>> createEntries(
            @PathVariable("id") Long topicId,
            @ModelAttribute EntryDto.CreateEntriesRequest request
    ){

        entryService.createEntries(topicId, request);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }
}

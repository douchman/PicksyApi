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

    @PostMapping("vstopic/{id}/entries/dummy")
    public ResponseEntity<SingleResponseDto<Integer>> createDummyEntries(
            @PathVariable("id") Long topicId
    ){

        entryService.createDummyEntries(topicId);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }

    @GetMapping("vstopic/{id}/entries")
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

    @PatchMapping("vstopic/{topicId}/entries/{entryId}")
    public ResponseEntity<SingleResponseDto<Integer>> updateEntry(
            @PathVariable("topicId") Long topicId,
            @PathVariable("entryId") Long entryId,
            @ModelAttribute EntryDto.UpdateEntryRequest request
    ){
        entryService.updateEntries(topicId, entryId, request);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }
}

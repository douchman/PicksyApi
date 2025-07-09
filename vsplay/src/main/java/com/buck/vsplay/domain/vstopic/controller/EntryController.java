package com.buck.vsplay.domain.vstopic.controller;


import com.buck.vsplay.domain.entry.dto.EntryDto;
import com.buck.vsplay.domain.entry.service.impl.EntryService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("topics")
public class EntryController {

    private final EntryService entryService;

    @GetMapping("{id}/entries")
    public ResponseEntity<SingleResponseDto<EntryDto.EntryList>> getAllEntries(
            @PathVariable("id") Long topicId
    ) {

        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), entryService.getEntriesByTopicId(topicId)) ,HttpStatus.OK);
    }

    @PostMapping("{id}/entries")
    public ResponseEntity<SingleResponseDto<Integer>> createEntries(
            @PathVariable("id") Long topicId,
            @RequestBody EntryDto.CreateEntriesRequest request
    ){

        entryService.createEntries(topicId, request);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PatchMapping("{topicId}/entries")
    public ResponseEntity<SingleResponseDto<Integer>> updateEntry(
            @PathVariable("topicId") Long topicId,
            @RequestBody EntryDto.UpdateEntryRequest request
    ){
        entryService.updateEntries(topicId, request);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }
}

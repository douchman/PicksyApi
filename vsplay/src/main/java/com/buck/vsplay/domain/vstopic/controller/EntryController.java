package com.buck.vsplay.domain.vstopic.controller;


import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.service.impl.EntryService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;

    @PostMapping("vstopic/entries")
    public ResponseEntity<SingleResponseDto<Integer>> createEntries(
            @ModelAttribute EntryDto.createEntriesRequest request
    ){

        entryService.createEntries(request);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }
}

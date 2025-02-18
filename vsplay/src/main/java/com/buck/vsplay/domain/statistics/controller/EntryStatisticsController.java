package com.buck.vsplay.domain.statistics.controller;


import com.buck.vsplay.domain.statistics.dto.EntryStatisticsDto;
import com.buck.vsplay.domain.statistics.service.impl.EntryStatisticsService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("statistics/topics")
public class EntryStatisticsController {

    private final EntryStatisticsService entryStatisticsService;

    @GetMapping("{topicId}/entries")
    public ResponseEntity<SingleResponseDto<EntryStatisticsDto.EntryStatWithEntryInfoList>> getEntriesStatistics(@PathVariable("topicId") Long topicId) {

        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), entryStatisticsService.getEntryStatisticsWithEntryInfo(topicId)), HttpStatus.OK);
    }
}

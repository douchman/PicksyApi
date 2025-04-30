package com.buck.vsplay.domain.statistics.controller;


import com.buck.vsplay.domain.statistics.dto.EntryVersusStatisticsDto;
import com.buck.vsplay.domain.statistics.service.impl.EntryVersusStatisticsService;
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
@RequestMapping("statistics/versus/topics/")
public class EntryVersusStatisticsController {

    private final EntryVersusStatisticsService entryVersusStatisticsService;

    @GetMapping("{topicId}/entries/{entryId}")
    public ResponseEntity<SingleResponseDto<EntryVersusStatisticsDto.EntryVersusStatisticsResponse>> getEntryVersusStatistics(
            @PathVariable("topicId") Long topicId,
            @PathVariable("entryId") Long entryId){

        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), entryVersusStatisticsService.getEntryVersusStatistics(topicId, entryId)), HttpStatus.OK);

    }
}

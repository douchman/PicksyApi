package com.buck.vsplay.domain.statistics.controller;


import com.buck.vsplay.domain.statistics.dto.TournamentStatisticsDto;
import com.buck.vsplay.domain.statistics.service.impl.TournamentStatisticsService;
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
public class TournamentStatisticsController {

    private final TournamentStatisticsService tournamentStatisticsService;

    @GetMapping("{topicId}/tournaments")
    public ResponseEntity<SingleResponseDto<TournamentStatisticsDto.TournamentStatisticsResponse>> getTournamentStatistics(@PathVariable("topicId") Long topicId) {

        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), tournamentStatisticsService.getTournamentStatistics(topicId)), HttpStatus.OK);
    }
}

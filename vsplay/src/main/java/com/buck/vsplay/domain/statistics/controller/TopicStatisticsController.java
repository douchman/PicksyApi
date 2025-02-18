package com.buck.vsplay.domain.statistics.controller;


import com.buck.vsplay.domain.statistics.dto.TopicStatisticsDto;
import com.buck.vsplay.domain.statistics.service.impl.TopicStatisticsService;
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
public class TopicStatisticsController {

    private final TopicStatisticsService topicStatisticsService;

    @GetMapping("{topicId}")
    public ResponseEntity<SingleResponseDto<TopicStatisticsDto.TopicStatisticsResponse>> getTopicStatistics(@PathVariable("topicId") Long topicId) {
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), topicStatisticsService.getTopicStatistics(topicId)), HttpStatus.OK);
    }
}

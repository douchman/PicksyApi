package com.buck.vsplay.domain.match.controller;

import com.buck.vsplay.domain.match.dto.EntryMatchDto;
import com.buck.vsplay.domain.match.dto.TopicPlayRecordDto;
import com.buck.vsplay.domain.match.service.impl.MatchService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("topics")
public class MatchController {

    private final MatchService matchService;

    @PostMapping("{topicId}/play-records")
    public ResponseEntity<SingleResponseDto<TopicPlayRecordDto.PlayRecordResponse>> recordTopicPlay(
            @PathVariable("topicId") Long topicId,
            @RequestBody TopicPlayRecordDto.PlayRecordRequest playRecordRequest
    ){
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), matchService.createTopicPlayRecord(topicId, playRecordRequest)), HttpStatus.OK);
    }

    @GetMapping("play-records/{playRecordId}/matches")
    public ResponseEntity<SingleResponseDto<EntryMatchDto.EntryMatchResponse>> getEntryMatch(
            @PathVariable("playRecordId") Long playRecordId
    ){
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(),matchService.getEntryMatch(playRecordId)), HttpStatus.OK);
    }

    @PatchMapping("play-records/{playRecordId}/matches/{matchId}")
    public ResponseEntity<SingleResponseDto<EntryMatchDto.UpdateEntryMatchResultResponse>> updateEntryMatch(
            @PathVariable("playRecordId") Long playRecordId,
            @PathVariable("matchId") Long matchId,
            @RequestBody @Validated EntryMatchDto.EntryMatchResultRequest entryMatchResultRequest
    ){
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), matchService.updateEntryMatchResult(playRecordId, matchId, entryMatchResultRequest)), HttpStatus.OK);
    }
}

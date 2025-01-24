package com.buck.vsplay.domain.vstopic.controller;

import com.buck.vsplay.domain.vstopic.dto.EntryMatchDto;
import com.buck.vsplay.domain.vstopic.dto.TopicPlayRecordDto;
import com.buck.vsplay.domain.vstopic.service.impl.MatchService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping("vstopic/{topicId}/play-record")
    public ResponseEntity<SingleResponseDto<TopicPlayRecordDto.PlayRecordResponse>> recordTopicPlay(
            @PathVariable("topicId") Long topicId,
            @RequestBody TopicPlayRecordDto.PlayRecordRequest playRecordRequest
    ){
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), matchService.createTopicPlayRecord(topicId, playRecordRequest)), HttpStatus.OK);
    }

    @GetMapping("vstopic/play-record/{playRecordId}/match")
    public ResponseEntity<SingleResponseDto<EntryMatchDto.EntryMatchResponse>> getEntryMatch(
            @PathVariable("playRecordId") Long playRecordId
    ){
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(),matchService.getEntryMatch(playRecordId)), HttpStatus.OK);
    }

    @PatchMapping("vstopic/play-record/{playRecordId}/match/{matchId}")
    public ResponseEntity<SingleResponseDto<Integer>> updateEntryMatch(
            @PathVariable("playRecordId") Long playRecordId,
            @PathVariable("matchId") Long matchId,
            @RequestBody @Validated EntryMatchDto.EntryMatchResultRequest entryMatchResultRequest
    ){
        matchService.updateEntryMatchResult(playRecordId, matchId, entryMatchResultRequest);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }
}

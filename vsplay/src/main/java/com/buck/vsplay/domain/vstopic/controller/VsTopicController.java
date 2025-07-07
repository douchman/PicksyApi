package com.buck.vsplay.domain.vstopic.controller;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.service.impl.VsTopicService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("topics")
public class VsTopicController {
    private final VsTopicService vsTopicService;

    @GetMapping("visibilities")
    public ResponseEntity<SingleResponseDto<List<VsTopicDto.TopicVisibility>>> getTopicVisibilities(){
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), vsTopicService.getTopicVisibilities()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<SingleResponseDto<VsTopicDto.VsTopicSearchResponse>> searchPublicVsTopic(
            VsTopicDto.VsTopicSearchRequest vsTopicSearchRequest
    ) {
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), vsTopicService.searchPublicVsTopic(vsTopicSearchRequest)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SingleResponseDto<VsTopicDto.VsTopicCreateResponse>> createTopic(
            @RequestBody @Valid VsTopicDto.VsTopicCreateRequest vsTopicDCreateVsTopicRequest) {

        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), vsTopicService.createVsTopic(vsTopicDCreateVsTopicRequest)),HttpStatus.OK );
    }

    @PatchMapping("{id}")
    public ResponseEntity<SingleResponseDto<Integer>> updateTopic(
            @PathVariable("id") Long topicId,
            @RequestBody VsTopicDto.VsTopicUpdateRequest topicDUpdateVsTopicRequest
    ){
        vsTopicService.updateVsTopic(topicId, topicDUpdateVsTopicRequest);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()),HttpStatus.OK );
    }

    @GetMapping("{id}")
    public ResponseEntity<SingleResponseDto<VsTopicDto.VsTopicDetailWithTournamentsResponse>> getTopicDetailWithTournaments (
            @PathVariable("id") Long topicId
    ) {
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), vsTopicService.getVsTopicDetailWithTournaments(topicId)),HttpStatus.OK );
    }
}

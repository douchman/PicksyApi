package com.buck.vsplay.domain.vstopic.controller;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.service.impl.VsTopicService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("vstopic")
public class VsTopicController {
    private final VsTopicService vsTopicService;

    @PostMapping
    public ResponseEntity<SingleResponseDto<Integer>> createTopic(
            @ModelAttribute @Valid VsTopicDto.VsTopicCreateRequest vsTopicDCreateVsTopicRequest) {

        vsTopicService.createVsTopic(vsTopicDCreateVsTopicRequest);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()),HttpStatus.OK );
    }

    @PatchMapping("{id}")
    public ResponseEntity<SingleResponseDto<Integer>> updateTopic(
            @PathVariable("id") Long topicId,
            @ModelAttribute VsTopicDto.VsTopicUpdateRequest topicDUpdateVsTopicRequest
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

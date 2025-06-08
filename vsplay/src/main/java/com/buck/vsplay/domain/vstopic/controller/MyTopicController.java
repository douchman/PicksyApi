package com.buck.vsplay.domain.vstopic.controller;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.service.impl.VsTopicService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("my")
public class MyTopicController {
    private final VsTopicService vsTopicService;

    @GetMapping("topics")
    public ResponseEntity<SingleResponseDto<VsTopicDto.MyTopicsResponse>> myVsTopics(
            VsTopicDto.VsTopicSearchRequest vsTopicSearchRequest
    ) {
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), vsTopicService.getMyVsTopics(vsTopicSearchRequest)), HttpStatus.OK);
    }
}

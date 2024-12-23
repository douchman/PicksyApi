package com.buck.vsplay.domain.vstopic.controller;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import com.buck.vsplay.domain.vstopic.service.impl.VsTopicService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("vstopic")
public class VsTopicController {
    private final VsTopicService vsTopicService;

    @PostMapping
    public ResponseEntity<SingleResponseDto<Integer>> createTopic(
            @RequestBody @Valid VsTopicDto.VsTopicCreateRequest vsTopicDCreateVsTopicRequest) {

        vsTopicService.createVsTopic(vsTopicDCreateVsTopicRequest);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()),HttpStatus.OK );
    }


}

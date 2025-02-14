package com.buck.vsplay.domain.vstopic.controller;


import com.buck.vsplay.domain.vstopic.dto.TopicCommentDto;
import com.buck.vsplay.domain.vstopic.service.impl.TopicCommentService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("vstopic")
public class CommentController {

    private final TopicCommentService topicCommentService;


    @PostMapping("{topicId}/comments")
    public ResponseEntity<SingleResponseDto<Integer>> createTopicComment(
        @PathVariable("topicId") Long topicId,
        @Valid TopicCommentDto.CommentCreateRequest commentCreateRequest
    ){

        topicCommentService.createTopicComment(topicId, commentCreateRequest);
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value()), HttpStatus.OK);
    }

}

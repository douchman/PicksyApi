package com.buck.vsplay.domain.vstopic.controller;


import com.buck.vsplay.domain.vstopic.dto.TopicCommentDto;
import com.buck.vsplay.domain.vstopic.service.impl.TopicCommentService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("topics")
public class CommentController {

    private final TopicCommentService topicCommentService;


    @GetMapping("{topicId}/comments")
    public ResponseEntity<SingleResponseDto<TopicCommentDto.CommentSearchResponse>> searchTopicComments(
            @PathVariable("topicId") Long topicId,
            TopicCommentDto.CommentSearchRequest commentSearchRequest
    ){
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), topicCommentService.searchTopicCommentList(topicId, commentSearchRequest)), HttpStatus.OK);
    }

    @PostMapping("{topicId}/comments")
    public ResponseEntity<SingleResponseDto<TopicCommentDto.CommentCreateResponse>> createTopicComment(
        @PathVariable("topicId") Long topicId,
        @RequestBody @Valid TopicCommentDto.CommentCreateRequest commentCreateRequest
    ){
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), topicCommentService.createTopicComment(topicId, commentCreateRequest)), HttpStatus.OK);
    }

}

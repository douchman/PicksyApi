package com.buck.vsplay.domain.comment.service;

import com.buck.vsplay.domain.comment.dto.TopicCommentDto;

public interface ITopicCommentService {
    TopicCommentDto.CommentCreateResponse createTopicComment(Long topicId, TopicCommentDto.CommentCreateRequest commentCreateRequest);
    TopicCommentDto.CommentSearchResponse searchTopicCommentList(Long topicId, TopicCommentDto.CommentSearchRequest commentSearchRequest);
}

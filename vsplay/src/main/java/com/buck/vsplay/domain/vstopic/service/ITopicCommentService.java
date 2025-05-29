package com.buck.vsplay.domain.vstopic.service;

import com.buck.vsplay.domain.vstopic.dto.TopicCommentDto;

public interface ITopicCommentService {
    TopicCommentDto.CommentCreateResponse createTopicComment(Long topicId, TopicCommentDto.CommentCreateRequest commentCreateRequest);
    TopicCommentDto.CommentSearchResponse searchTopicCommentList(Long topicId, TopicCommentDto.CommentSearchRequest commentSearchRequest);
}

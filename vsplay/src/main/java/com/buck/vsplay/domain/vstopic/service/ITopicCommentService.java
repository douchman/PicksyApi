package com.buck.vsplay.domain.vstopic.service;

import com.buck.vsplay.domain.vstopic.dto.TopicCommentDto;

public interface ITopicCommentService {
    void createTopicComment(Long topicId, TopicCommentDto.CommentCreateRequest commentCreateRequest);
}

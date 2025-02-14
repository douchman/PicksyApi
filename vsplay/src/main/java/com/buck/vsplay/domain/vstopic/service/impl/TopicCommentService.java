package com.buck.vsplay.domain.vstopic.service.impl;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.vstopic.dto.TopicCommentDto;
import com.buck.vsplay.domain.vstopic.entity.TopicComment;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.repository.TopicCommentRepository;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.domain.vstopic.service.ITopicCommentService;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicCommentService implements ITopicCommentService {

    private final AuthUserService authUserService;
    private final VsTopicRepository topicRepository;
    private final TopicCommentRepository topicCommentRepository;


    @Override
    public void createTopicComment(Long topicId, TopicCommentDto.CommentCreateRequest commentCreateRequest) {

        Optional<Member> authUser = authUserService.getAuthUserOptional();
        VsTopic vsTopic = topicRepository.findById(topicId).orElseThrow(() ->
                new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));

        topicCommentRepository.save(TopicComment.builder()
                .topic(vsTopic)
                .member(authUser.orElse(null))
                .content(commentCreateRequest.getContent())
                .build());

    }

}

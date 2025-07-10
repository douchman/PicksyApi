package com.buck.vsplay.domain.comment.service.impl;

import com.buck.vsplay.domain.comment.dto.TopicCommentDto;
import com.buck.vsplay.domain.comment.entity.TopicComment;
import com.buck.vsplay.domain.comment.mapper.CommentMapper;
import com.buck.vsplay.domain.comment.service.ITopicCommentService;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.comment.repository.TopicCommentRepository;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.domain.vstopic.service.finder.TopicFinder;
import com.buck.vsplay.global.dto.Pagination;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import com.buck.vsplay.global.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TopicCommentService implements ITopicCommentService {

    private final AuthUserService authUserService;
    private final VsTopicRepository topicRepository;
    private final TopicCommentRepository topicCommentRepository;
    private final TopicFinder topicFinder;
    private final CommentMapper commentMapper;

    @Override
    public TopicCommentDto.CommentCreateResponse createTopicComment(Long topicId, TopicCommentDto.CommentCreateRequest commentCreateRequest) {

        VsTopic vsTopic = topicRepository.findByIdAndDeletedFalse(topicId).orElseThrow(() ->
                new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));

        TopicComment savedComment = topicCommentRepository.save(TopicComment.builder()
                .topic(vsTopic)
                .member(authUserService.getAuthUserOptional().orElse(null))
                .author(commentCreateRequest.getAuthor())
                .content(commentCreateRequest.getContent())
                .build());

        return TopicCommentDto.CommentCreateResponse.builder()
                .author(savedComment.getAuthor())
                .content(savedComment.getContent())
                .createdAt(DateTimeUtil.formatDateToSting(savedComment.getCreatedAt()))
                .build();
    }

    @Override
    public TopicCommentDto.CommentSearchResponse searchTopicCommentList(Long topicId, TopicCommentDto.CommentSearchRequest commentSearchRequest) {

        int page = Math.max(commentSearchRequest.getPage() - 1, 0) ;

        topicFinder.findExistingById(topicId);

        Page<TopicComment> topicCommentWithPage = topicCommentRepository.findCommentByTopicIdAndContentOrderByNewest(topicId, commentSearchRequest.getKeyword(), PageRequest.of(page, commentSearchRequest.getSize()));

        return TopicCommentDto.CommentSearchResponse.builder()
                .commentList(commentMapper.toDtoListFromEntityList(topicCommentWithPage.getContent()))
                .pagination(Pagination.builder()
                        .totalPages(topicCommentWithPage.getTotalPages())
                        .totalItems(topicCommentWithPage.getTotalElements())
                        .currentPage(topicCommentWithPage.getNumber() + 1)
                        .pageSize(topicCommentWithPage.getSize())
                        .build())
                .build();
    }

}

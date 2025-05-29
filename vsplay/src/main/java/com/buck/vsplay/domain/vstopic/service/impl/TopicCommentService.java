package com.buck.vsplay.domain.vstopic.service.impl;

import com.buck.vsplay.domain.vstopic.dto.TopicCommentDto;
import com.buck.vsplay.domain.vstopic.entity.TopicComment;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.repository.TopicCommentRepository;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.domain.vstopic.service.ITopicCommentService;
import com.buck.vsplay.domain.vstopic.specification.TopicCommentSpecification;
import com.buck.vsplay.global.dto.Pagination;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import com.buck.vsplay.global.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicCommentService implements ITopicCommentService {

    private final AuthUserService authUserService;
    private final VsTopicRepository topicRepository;
    private final TopicCommentRepository topicCommentRepository;


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
        if( !topicRepository.existsById(topicId) ) {
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND);
        }

        Page<TopicComment> topicCommentPage = topicCommentRepository.findAll(
                TopicCommentSpecification.withAllFilters(topicId, commentSearchRequest.getKeyword(), false),
                PageRequest.of(page, commentSearchRequest.getSize(), Sort.by(Sort.Direction.DESC, "createdAt")));

        List<TopicCommentDto.Comment> topicCommentList = new ArrayList<>();

        // entity -> dto mapping
        for (TopicComment topicComment : topicCommentPage.getContent()) {
            topicCommentList.add(
                    TopicCommentDto.Comment.builder()
                            .author(topicComment.getAuthor())
                            .content(topicComment.getContent())
                            .createdAt(DateTimeUtil.formatDateToSting(topicComment.getCreatedAt())).build()
            );
        }

        return TopicCommentDto.CommentSearchResponse.builder()
                .commentList(topicCommentList)
                .pagination(Pagination.builder()
                        .totalPages(topicCommentPage.getTotalPages())
                        .totalItems(topicCommentPage.getTotalElements())
                        .currentPage(topicCommentPage.getNumber() + 1)
                        .pageSize(topicCommentPage.getSize())
                        .build())
                .build();
    }

}

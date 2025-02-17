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
                .author(commentCreateRequest.getAuthor())
                .content(commentCreateRequest.getContent())
                .build());

    }

    @Override
    public TopicCommentDto.CommentSearchResponse searchTopicCommentList(Long topicId, TopicCommentDto.CommentSearchRequest commentSearchRequest) {

        int page = Math.max(commentSearchRequest.getPage() - 1, 0) ;
        if( !topicRepository.existsById(topicId) ) {
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND);
        }

        Page<TopicComment> topicCommentPage = topicCommentRepository.findAll(
                TopicCommentSpecification.keywordFilter(commentSearchRequest.getKeyword()),
                PageRequest.of(page, commentSearchRequest.getSize(), Sort.by(Sort.Direction.DESC, "createdAt")));

        List<TopicCommentDto.Comment> topicCommentList = new ArrayList<>();

        // entity -> dto mapping
        for (TopicComment topicComment : topicCommentPage.getContent()) {
            topicCommentList.add(
                    TopicCommentDto.Comment.builder()
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

package com.buck.vsplay.domain.comment.mapper;

import com.buck.vsplay.domain.comment.dto.TopicCommentDto;
import com.buck.vsplay.domain.comment.entity.TopicComment;
import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.global.util.DateTimeUtil;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = DateTimeUtil.class)
public interface CommentMapper {

    default TopicComment toEntityFromCommentCreateRequest(VsTopic topic, Member member, TopicCommentDto.CommentCreateRequest request){
        if(request == null) return null;
        return TopicComment.builder()
                .topic(topic)
                .member(member)
                .author(request.getAuthor())
                .content(request.getContent())
                .build();
    }

    default TopicCommentDto.Comment toDtoFromEntity(TopicComment topicComment){
        if(topicComment == null) return null;
        return TopicCommentDto.Comment.builder()
                .author(topicComment.getAuthor())
                .content(topicComment.getContent())
                .createdAt(DateTimeUtil.formatDateToSting(topicComment.getCreatedAt()))
                .build();
    }

    default List<TopicCommentDto.Comment> toDtoListFromEntityList(List<TopicComment> commentEntityList){
        return commentEntityList.stream()
                .map(this::toDtoFromEntity)
                .toList();
    }
}

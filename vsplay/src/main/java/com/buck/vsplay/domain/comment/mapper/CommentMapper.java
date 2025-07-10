package com.buck.vsplay.domain.comment.mapper;

import com.buck.vsplay.domain.comment.dto.TopicCommentDto;
import com.buck.vsplay.domain.comment.entity.TopicComment;
import com.buck.vsplay.global.util.DateTimeUtil;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = DateTimeUtil.class)
public interface CommentMapper {

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

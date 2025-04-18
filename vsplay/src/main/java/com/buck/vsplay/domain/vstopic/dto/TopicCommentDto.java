package com.buck.vsplay.domain.vstopic.dto;


import com.buck.vsplay.global.dto.Pagination;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicCommentDto {

    @Getter
    @Setter
    public static class CommentCreateRequest{
        String author = "익명";
        @NotNull(message = "작성할 코멘트가 비었습니다.")
        String content;
    }

    @Getter
    @Setter
    public static class CommentSearchRequest{
        String keyword;
        Integer page = 1;
        Integer size = 20;
    }

    @Getter
    @Setter
    @Builder
    public static class Comment{
        String author;
        String content;
        String createdAt;
    }

    @Getter
    @Setter
    @Builder
    public static class CommentSearchResponse{
        List<Comment> commentList;
        Pagination pagination;
    }

    @Getter
    @Setter
    @Builder
    public static class CommentCreateResponse{
        String author;
        String content;
        String createdAt;
    }
}

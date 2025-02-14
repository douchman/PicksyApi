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
        @NotNull(message = "작성할 코멘트가 비었습니다.")
        String content;
    }

    @Getter
    @Setter
    public static class CommentSearchRequest{
        String keyword;
        Integer page;
        Integer size;
    }

    @Getter
    @Setter
    @Builder
    public static class Comment{
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

}

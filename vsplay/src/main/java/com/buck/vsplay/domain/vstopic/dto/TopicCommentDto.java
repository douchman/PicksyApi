package com.buck.vsplay.domain.vstopic.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicCommentDto {

    @Getter
    @Setter
    public static class CommentCreateRequest{
        @NotNull(message = "작성할 코멘트가 비었습니다.")
        String content;
    }

}

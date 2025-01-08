package com.buck.vsplay.domain.vstopic.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VsTopicDto {

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VsTopic{
        private Long id;
        private String title;
        private String subject;
        private String description;
    }

    @Data
    public static class VsTopicCreateRequest{
        @NotNull(message = "대결 제목은 필수 입력 항목입니다.")
        private String title;
        @NotNull(message = "대결 주제는 필수 입력 항목입니다.")
        private String subject;
        private String description;
    }

}

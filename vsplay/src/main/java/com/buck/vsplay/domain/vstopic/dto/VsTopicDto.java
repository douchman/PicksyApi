package com.buck.vsplay.domain.vstopic.dto;


import com.buck.vsplay.global.constants.ModerationStatus;
import com.buck.vsplay.global.constants.SortBy;
import com.buck.vsplay.global.constants.Visibility;
import com.buck.vsplay.global.dto.Pagination;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VsTopicDto {

    @Getter
    @Setter
    @Builder
    public static class TopicVisibility {
        String visibility;
        String description;
    }

    @Getter
    @Setter
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VsTopic{
        private Long id;
        private String title;
        private String subject;
        private String description;
        private String thumbnail;
        private Visibility visibility;
    }

    @Getter
    @Setter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    public static class VsTopicWithModeration extends VsTopic{
        private ModerationStatus moderationStatus;
    }
    @Getter
    @Setter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    public static class VsTopicWithAccessCode extends VsTopic{
        private String accessCode;
    }

    @Getter
    @Setter
    public static class VsTopicCreateRequest{
        @NotNull(message = "대결 제목은 필수 입력 항목입니다.")
        private String title;

        @NotNull(message = "대결 주제는 필수 입력 항목입니다.")

        private String subject;
        private String description;
        private String thumbnail;

        @NotNull(message = "공개 범위는 필수 입력 항목입니다.")
        private Visibility visibility;

        private String accessCode;
    }

    @Getter
    @Setter
    @Builder
    public static class VsTopicCreateResponse{
        private Long topicId;
        private String title;
        private String subject;
        private String description;
        private Visibility visibility;
    }

    @Getter
    @Setter
    public static class VsTopicUpdateRequest{
        private String title;
        private String subject;
        private String description;
        private String thumbnail;
        private Visibility visibility;
        private String accessCode;
    }

    @Getter
    @Setter
    public static class Tournament{
        Integer tournamentStage;
        String tournamentName;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    public static class VsTopicDetailWithAccessCodeResponse{
        VsTopicWithAccessCode topic;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    public static class VsTopicDetailWithTournamentsResponse{
        VsTopicWithModeration topic;
        List<Tournament> tournamentList;
    }

    @Getter
    @Setter
    @Builder
    public static class VsTopicSearchResponse{
        List<VsTopicDto.VsTopic> topicList;
        Pagination pagination;
    }

    @Getter
    @Setter
    @Builder
    public static class MyTopicsResponse{
        List<VsTopicDto.VsTopicWithModeration> topicList;
        Pagination pagination;
    }

    @Getter
    @Setter
    public static class VsTopicSearchRequest{
        private String keyword;
        private Integer page = 1 ;
        private Integer size = 20;
        private SortBy searchSortBy = SortBy.LATEST;
        private Visibility visibility;
    }
}

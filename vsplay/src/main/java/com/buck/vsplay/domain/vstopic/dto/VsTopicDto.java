package com.buck.vsplay.domain.vstopic.dto;


import com.buck.vsplay.global.constants.ModerationStatus;
import com.buck.vsplay.global.constants.SortBy;
import com.buck.vsplay.global.constants.Visibility;
import com.buck.vsplay.global.dto.Pagination;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VsTopicDto {

    @Getter
    @Setter
    @Builder
    public static class TopicVisibility {
        String visibility;
        String description;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VsTopic{
        private Long id;
        private String title;
        private String subject;
        private String description;
        private String thumbnail;
        private Visibility visibility;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class VsTopicWithThumbnail extends VsTopic{
        String thumbnail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class VsTopicWithModeration extends VsTopicWithThumbnail{
        private ModerationStatus moderationStatus;
    }

    @Data
    public static class VsTopicCreateRequest{
        @NotNull(message = "대결 제목은 필수 입력 항목입니다.")
        private String title;

        @NotNull(message = "대결 주제는 필수 입력 항목입니다.")

        private String subject;
        private String description;
        private MultipartFile thumbnail;

        @NotNull(message = "공개 범위는 필수 입력 항목입니다.")
        private Visibility visibility;
    }

    @Data
    @Builder
    public static class VsTopicCreateResponse{
        private Long topicId;
        private String title;
        private String subject;
        private String description;
        private Visibility visibility;
    }

    @Data
    public static class VsTopicUpdateRequest{
        private String title;
        private String subject;
        private String description;
        private MultipartFile thumbnail;
        private Visibility visibility;
    }

    @Data
    public static class Tournament{
        Integer tournamentStage;
        String tournamentName;
    }

    @Data
    @ToString
    public static class VsTopicDetailWithTournamentsResponse{
        VsTopicWithModeration topic;
        List<Tournament> tournamentList = new ArrayList<>();
    }

    @Getter
    @Builder
    public static class VsTopicSearchResponse{
        List<VsTopicDto.VsTopicWithThumbnail> topicList;
        Pagination pagination;
    }

    @Getter
    @Builder
    public static class MyTopicsResponse{
        List<VsTopicDto.VsTopicWithModeration> topicList;
        Pagination pagination;
    }


    @Data
    public static class VsTopicSearchRequest{
        private String keyword;
        private Integer page = 1 ;
        private Integer size = 20;
        private SortBy searchSortBy = SortBy.LATEST;
        private Visibility visibility;
    }

    @Data
    @Builder
    public static class VsTopicUnlistedLinkResponse{
        private String link;
    }
}

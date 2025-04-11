package com.buck.vsplay.domain.statistics.dto;


import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicStatisticsDto {

    @Setter
    @Getter
    public static class TopicStatistics{
        Integer totalMatches;
        Integer completedMatches;
        EntryDto.Entry mostPopularEntry;
        String firstPlayedAt;
        String lastPlayedAt;
        String createdAt;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @Builder
    public static class TopicStatisticsResponse{
        VsTopicDto.VsTopic topic;
        TopicStatistics topicStatistics;
    }
}

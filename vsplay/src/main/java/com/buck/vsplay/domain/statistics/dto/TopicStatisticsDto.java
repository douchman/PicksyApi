package com.buck.vsplay.domain.statistics.dto;


import com.buck.vsplay.domain.vstopic.dto.EntryDto;
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
    public static class TopicStatisticsResponse{
        TopicStatistics topicStatistics;
    }
}

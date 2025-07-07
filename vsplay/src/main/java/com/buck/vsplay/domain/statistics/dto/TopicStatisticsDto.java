package com.buck.vsplay.domain.statistics.dto;


import com.buck.vsplay.domain.vstopic.dto.VsTopicDto;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicStatisticsDto {

    @Setter
    @Getter
    public static class TopicStatistics{
        Integer entryCount;
        Integer totalMatches;
        Integer completedMatches;
        String firstPlayedAt;
        String lastPlayedAt;
        String createdAt;
    }

    @Setter
    @Getter
    @Builder
    public static class TopicStatisticsResponse{
        VsTopicDto.VsTopic topic;
        List<TournamentStatisticsDto.TournamentStatistics> tournamentStatistics;
        TopicStatistics topicStatistics;
    }
}

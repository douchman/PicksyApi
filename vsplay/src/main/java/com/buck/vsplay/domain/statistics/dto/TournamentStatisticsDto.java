package com.buck.vsplay.domain.statistics.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TournamentStatisticsDto {

    @Getter
    @Setter
    public static class TournamentStatistics {
        String tournamentName;
        Integer tournamentStage;
        Integer stageMatches;
    }

    @Getter
    @Setter
    @Builder
    public static class TournamentStatisticsResponse{
        List<TournamentStatistics> tournamentStatistics;
    }
}

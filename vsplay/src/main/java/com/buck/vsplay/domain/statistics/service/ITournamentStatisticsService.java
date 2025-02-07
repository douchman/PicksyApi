package com.buck.vsplay.domain.statistics.service;


import com.buck.vsplay.domain.statistics.dto.TournamentStatisticsDto;
import com.buck.vsplay.domain.vstopic.entity.TopicTournament;

public interface ITournamentStatisticsService {
    void createTournamentStatistics(TopicTournament topicTournament);
    void recordMatchStat(TopicTournament topicTournament);

    TournamentStatisticsDto.TournamentStatisticsResponse getTournamentStatistics(Long topicId);
}

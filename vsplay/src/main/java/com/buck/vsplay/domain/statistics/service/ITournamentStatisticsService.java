package com.buck.vsplay.domain.statistics.service;


import com.buck.vsplay.domain.vstopic.entity.TopicTournament;

public interface ITournamentStatisticsService {
    void createTournamentStatistics(TopicTournament topicTournament);
}

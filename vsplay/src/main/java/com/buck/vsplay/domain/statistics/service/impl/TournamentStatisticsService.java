package com.buck.vsplay.domain.statistics.service.impl;

import com.buck.vsplay.domain.statistics.entity.TournamentStatistics;
import com.buck.vsplay.domain.statistics.event.TournamentEvent;
import com.buck.vsplay.domain.statistics.repository.TournamentStatisticsRepository;
import com.buck.vsplay.domain.statistics.service.ITournamentStatisticsService;
import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentStatisticsService implements ITournamentStatisticsService {

    private final TournamentStatisticsRepository tournamentStatisticsRepository;

    @EventListener // 생성된 토너먼트 엔티티에 대한 이벤트 구독
    public void handleTopicTournamentUpdated(TournamentEvent.CreateEvent topicCreateEvent) {
        createTournamentStatistics(topicCreateEvent.getTopicTournament());
    }


    @EventListener
    public void handleTopicTournamentPlayed(TournamentEvent.PlayEvent topicPlayEvent) {
        recordMatchStat(topicPlayEvent.getTopicTournament());
    }

    @Override
    public void createTournamentStatistics(TopicTournament topicTournament) {
        tournamentStatisticsRepository.save(TournamentStatistics.builder()
                .topicTournament(topicTournament)
                .tournamentStage(topicTournament.getTournamentStage())
                .build());

    }

    @Override
    public void recordMatchStat(TopicTournament topicTournament) {
        TournamentStatistics tournamentStatistics =
                tournamentStatisticsRepository.findByTournamentIdAndTournamentStage(topicTournament.getId(), topicTournament.getTournamentStage());

        tournamentStatistics.increaseStageMatches();

        tournamentStatisticsRepository.save(tournamentStatistics);
    }
}

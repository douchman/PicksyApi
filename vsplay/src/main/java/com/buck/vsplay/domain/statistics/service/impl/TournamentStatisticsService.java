package com.buck.vsplay.domain.statistics.service.impl;

import com.buck.vsplay.domain.statistics.dto.TournamentStatisticsDto;
import com.buck.vsplay.domain.statistics.entity.TournamentStatistics;
import com.buck.vsplay.domain.statistics.event.TournamentEvent;
import com.buck.vsplay.domain.statistics.mapper.TournamentStatisticsMapper;
import com.buck.vsplay.domain.statistics.repository.TournamentStatisticsRepository;
import com.buck.vsplay.domain.statistics.service.ITournamentStatisticsService;
import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TournamentStatisticsService implements ITournamentStatisticsService {

    private final TournamentStatisticsRepository tournamentStatisticsRepository;
    private final VsTopicRepository vsTopicRepository;
    private final TournamentStatisticsMapper tournamentStatisticsMapper;

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

    @Override
    public TournamentStatisticsDto.TournamentStatisticsResponse getTournamentStatistics(Long topicId) {

        if(!vsTopicRepository.existsByIdAndDeletedFalse(topicId)) {
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND);
        }

        return TournamentStatisticsDto.TournamentStatisticsResponse.builder()
                .tournamentStatistics(tournamentStatisticsMapper.toTournamentStatisticsDtoList(tournamentStatisticsRepository.findByTopicId(topicId)))
                .build();
    }
}

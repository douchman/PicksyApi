package com.buck.vsplay.domain.statistics.service.impl;

import com.buck.vsplay.domain.statistics.entity.TopicStatistics;
import com.buck.vsplay.domain.statistics.event.TopicEvent;
import com.buck.vsplay.domain.statistics.projection.MostPopularEntry;
import com.buck.vsplay.domain.statistics.repository.TopicStatisticsRepository;
import com.buck.vsplay.domain.statistics.service.ITopicStatisticsService;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TopicStatisticsService implements ITopicStatisticsService {

    private final TopicStatisticsRepository topicStatisticsRepository;

    @EventListener
    public void handleVsTopicCreated(TopicEvent.CreateEvent topiCreateEvent) {
        createTopicStatistics(topiCreateEvent.getTopic());
    }

    @EventListener
    public void handleVsTopicPlayed(TopicEvent.PlayEvent topiPlayEvent) {
        recordPlayStats(topiPlayEvent.getTopic());
    }


    @EventListener
    public void handleVsTopicPlayRecordCompleted(TopicEvent.PlayCompleteEvent topiPlayCompleteEvent) {
        recordCompletedMatchStats(topiPlayCompleteEvent.getTopic());
    }

    @Override
    public void createTopicStatistics(VsTopic vsTopic) {
        topicStatisticsRepository.save(TopicStatistics.builder()
                .vsTopic(vsTopic)
                .totalMatches(0)
                .totalPlayers(0)
                .completedMatches(0)
                .completedPlayers(0)
                .build());
    }

    @Override
    public void recordPlayStats(VsTopic vsTopic) {
        TopicStatistics topicStatistics = topicStatisticsRepository.findByVsTopic(vsTopic.getId());

        topicStatistics.increaseTotalMatches();
        topicStatistics.updatePlayedDates();

        topicStatisticsRepository.save(topicStatistics);
    }

    @Override
    public void recordCompletedMatchStats(VsTopic vsTopic) {
        TopicStatistics topicStatistics = topicStatisticsRepository.findByVsTopic(vsTopic.getId());

        List<MostPopularEntry> mostPopularEntries = topicStatisticsRepository.findMostPopularEntriesByTopicId(vsTopic.getId()); // 결승전에서 승리한 엔트리와 승리 횟수 조회
        MostPopularEntry mostPopularEntry = mostPopularEntries.get(0); // 가장 높은 승리 횟수를 기록한 엔트리 선별

        topicStatistics.increaseCompletedMatches();
        topicStatistics.setMostPopularEntry(mostPopularEntry.getEntry());

        topicStatisticsRepository.save(topicStatistics);
    }
}

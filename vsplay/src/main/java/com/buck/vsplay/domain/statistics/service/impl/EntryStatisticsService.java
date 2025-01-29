package com.buck.vsplay.domain.statistics.service.impl;

import com.buck.vsplay.domain.statistics.entity.EntryStatistics;
import com.buck.vsplay.domain.statistics.event.EntryEvent;
import com.buck.vsplay.domain.statistics.repository.EntryStatisticsRepository;
import com.buck.vsplay.domain.statistics.service.IEntryStatisticsService;
import com.buck.vsplay.domain.vstopic.entity.EntryMatch;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EntryStatisticsService implements IEntryStatisticsService {
    private final EntryStatisticsRepository entryStatisticsRepository;

    @EventListener
    public void handleEntryCrateEvent(EntryEvent.CreateEvent entryCreateEvent){
        createEntryStatistics(entryCreateEvent.getTopicEntryList());
    }

    @EventListener
    public void handleEntryMatchCompleteEvent(EntryEvent.MatchCompleteEvent entryMatchCompleteEvent){
        recordEntryMatchStats(entryMatchCompleteEvent.getEntryMatch());
    }

    @Override
    public void createEntryStatistics(List<TopicEntry> topicEntryList) {

        List<EntryStatistics> entryStatisticsList = new ArrayList<>();
        for( TopicEntry topicEntry : topicEntryList ){
            entryStatisticsList.add(EntryStatistics.builder()
                    .topicEntry(topicEntry)
                    .totalMatches(0)
                    .totalWins(0)
                    .totalLosses(0)
                    .winRate(0.0)
                    .highestTournament(0)
                    .build());
        }

        entryStatisticsRepository.saveAll(entryStatisticsList);
    }

    @Override
    public void recordEntryMatchStats(EntryMatch entryMatch) {

        EntryStatistics winnerEntry = entryStatisticsRepository.findByEntryId(entryMatch.getWinnerEntry().getId());
        EntryStatistics loserEntry = entryStatisticsRepository.findByEntryId(entryMatch.getLoserEntry().getId());

        winnerEntry.increaseTotalMatches();
        winnerEntry.increaseTotalWins();
        winnerEntry.calculateWinRate();

        loserEntry.increaseTotalMatches();
        loserEntry.increaseTotalLosses();
        loserEntry.calculateWinRate();

        entryStatisticsRepository.save(winnerEntry);
        entryStatisticsRepository.save(loserEntry);
    }
}

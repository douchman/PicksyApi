package com.buck.vsplay.domain.statistics.service.impl;

import com.buck.vsplay.domain.statistics.entity.EntryVersusStatistics;
import com.buck.vsplay.domain.statistics.event.EntryEvent;
import com.buck.vsplay.domain.statistics.repository.EntryVersusStatisticsRepository;
import com.buck.vsplay.domain.statistics.service.IEntryVersusStatisticsService;
import com.buck.vsplay.domain.vstopic.entity.EntryMatch;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EntryVersusStatisticsService implements IEntryVersusStatisticsService {

    private final EntryVersusStatisticsRepository entryVersusStatisticsRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void handleEntryMatchCompletedEventForVersusStats(EntryEvent.VersusStatisticsEvent matchCompleteEvent) {
        log.info(" ## handleEntryMatchCompletedEventForVersusStats execute ## ");
        upsertEntryVersusStatistics(matchCompleteEvent.getEntryMatch());
    }

    @Override
    public void upsertEntryVersusStatistics(EntryMatch entryMatch) {

        EntryVersusStatistics statsOfWinnerEntry = upsertStats(entryMatch.getWinnerEntry(), entryMatch.getLoserEntry(), true);
        EntryVersusStatistics statsOfLoserEntry = upsertStats(entryMatch.getLoserEntry(), entryMatch.getWinnerEntry(), false);

        entryVersusStatisticsRepository.save(statsOfWinnerEntry);
        entryVersusStatisticsRepository.save(statsOfLoserEntry);
    }

    private EntryVersusStatistics upsertStats(TopicEntry entry, TopicEntry opponentEntry, boolean isWin) {
        EntryVersusStatistics entryVersusStatistics = entryVersusStatisticsRepository.findByEntryIdAndOpponentEntryId(entry.getId(), opponentEntry.getId());

        if ( entryVersusStatistics == null ) {
            entryVersusStatistics = EntryVersusStatistics.builder()
                    .topicEntry(entry)
                    .opponentEntry(opponentEntry)
                    .totalMatches(1)
                    .wins(isWin ? 1 : 0)
                    .losses(isWin ? 0 : 1) // 패배 엔트리 기준 신규 삽입
                    .build();

            entryVersusStatistics.calculateWinRate(); // 승률 계산

        } else {
            entryVersusStatistics.increaseTotalMatches();
            if ( isWin ) {
                entryVersusStatistics.increaseWins();
            } else {
                entryVersusStatistics.increaseLosses();
            }
            entryVersusStatistics.calculateWinRate();
        }

        return entryVersusStatistics;
    }
}

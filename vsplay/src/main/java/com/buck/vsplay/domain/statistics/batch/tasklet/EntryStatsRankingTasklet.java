package com.buck.vsplay.domain.statistics.batch.tasklet;

import com.buck.vsplay.domain.statistics.entity.EntryStatistics;
import com.buck.vsplay.domain.statistics.repository.EntryStatisticsRepository;
import com.buck.vsplay.domain.statistics.util.EntryStatsRankingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EntryStatsRankingTasklet implements Tasklet {

    private final EntryStatisticsRepository entryStatisticsRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        processRanking(groupingByTopic(entryStatisticsRepository.finalAllWithTopicEntryAndTopic()));
        return RepeatStatus.FINISHED;
    }

    // topic id 를 기준으로 그룹화
    private Map<Long, List<EntryStatistics>> groupingByTopic(List<EntryStatistics> allEntryStatistics) {
        return allEntryStatistics.stream()
                .collect(Collectors.groupingBy(es -> es.getTopicEntry().getTopic().getId()));
    }

    // 엔트리 통계 순위 책정을 위한 랭킹 스코어 처리
    private void calculateEntryStatsRankScore(List<EntryStatistics> entryStatisticsList) {
        entryStatisticsList.forEach(es -> {
            double rankScore = EntryStatsRankingUtil.calculateRankingScore(
                    es.getWinRate(),
                    es.getTotalMatches(),
                    es.getTotalWins());
            es.setRankScore(rankScore);
        });
    }

    // 엔트리 통계 순위 책정
    private void setEntryStatsRank(List<EntryStatistics> entryStatisticsList){
        AtomicInteger rank = new AtomicInteger(1); // 등수 1부터 시작
        entryStatisticsList.stream()
                .sorted(Comparator.comparing(EntryStatistics::getRankScore).reversed())
                .forEach(es ->{
                    Integer newRank = rank.getAndIncrement(); // 새로운 랭크
                    Integer previousRank = es.getEntryRank() ; // 이전 랭크
                    if( !Objects.equals(previousRank, newRank) ) { // 랭크 변동이 있는 경우만 순위 갱신
                        log.info("\uD83D\uDCCA Entry Stats Rank 변경: entryId {} - {} → {}", es.getId(), es.getEntryRank(), newRank);
                        es.setEntryRank(newRank);
                    }
                });
    }

    // topic id 로 그룹화된 통계데이터를 순회하며 랭크 처리
    private void processRanking(Map<Long, List<EntryStatistics>> groupedByTopic) {
        // 2. 대결주제 기준으로 Main loop
        groupedByTopic.forEach((key, entryStatistics) -> {

            // 3. 1차 Sub Loop -> RankScore 계산
            calculateEntryStatsRankScore(entryStatistics);

            // 4. 2차 Sub Loop -> Rank 책정
            setEntryStatsRank(entryStatistics);

        });
    }
}

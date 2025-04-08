package com.buck.vsplay.domain.statistics.service.impl;

import com.buck.vsplay.domain.statistics.dto.EntryStatisticsDto;
import com.buck.vsplay.domain.statistics.entity.EntryStatistics;
import com.buck.vsplay.domain.statistics.event.EntryEvent;
import com.buck.vsplay.domain.statistics.mapper.EntryStatisticsMapper;
import com.buck.vsplay.domain.statistics.repository.EntryStatisticsRepository;
import com.buck.vsplay.domain.statistics.service.IEntryStatisticsService;
import com.buck.vsplay.domain.statistics.specification.EntryStatsSpecification;
import com.buck.vsplay.domain.vstopic.entity.EntryMatch;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.mapper.TopicEntryMapper;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.global.constants.MediaType;
import com.buck.vsplay.global.dto.Pagination;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EntryStatisticsService implements IEntryStatisticsService {

    private final EntryStatisticsRepository entryStatisticsRepository;
    private final VsTopicRepository vsTopicRepository;
    private final EntryStatisticsMapper entryStatisticsMapper;
    private final TopicEntryMapper topicEntryMapper;

    @EventListener
    public void handleEntryCrateEvent(EntryEvent.CreateEvent entryCreateEvent){
        createEntryStatistics(entryCreateEvent.getTopicEntryList());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
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

        Integer currentTournamentRound = entryMatch.getTournamentRound();

        EntryStatistics winnerEntry = entryStatisticsRepository.findByEntryId(entryMatch.getWinnerEntry().getId());
        EntryStatistics loserEntry = entryStatisticsRepository.findByEntryId(entryMatch.getLoserEntry().getId());

        winnerEntry.increaseTotalMatches();
        winnerEntry.increaseTotalWins();
        winnerEntry.calculateWinRate();
        winnerEntry.checkAndUpdateHighestTournament(currentTournamentRound);

        loserEntry.increaseTotalMatches();
        loserEntry.increaseTotalLosses();
        loserEntry.calculateWinRate();
        loserEntry.checkAndUpdateHighestTournament(currentTournamentRound);

        entryStatisticsRepository.save(winnerEntry);
        entryStatisticsRepository.save(loserEntry);
    }

    @Override
    public EntryStatisticsDto.EntryStatSearchResponse getEntryStatisticsWithEntryInfo(Long topicId, EntryStatisticsDto.EntryStatSearchRequest entryStatSearchRequest) {
        List<EntryStatisticsDto.EntryStatWithEntryInfo> entriesStatistics = new ArrayList<>();
        int page = Math.max(entryStatSearchRequest.getPage() - 1 , 0); // index 조정

        // 정렬 필터
        // totalMatches desc -> 총 대결 수가 많을수록 우선순위
        // totalWins desc -> 총 승리 횟수가 많을수록 우선순위
        // winRate desc -> 승률이 가장 높을수록 우선순위
        Specification<EntryStatistics> entryStatsSpecification =
                EntryStatsSpecification.idFilter(topicId)
                        .and(EntryStatsSpecification.orderFilter(true, true, true));

        if(!vsTopicRepository.existsById(topicId)){
            throw new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND);
        }

        Page<EntryStatistics> entryStatistics = entryStatisticsRepository.findAll(
                entryStatsSpecification,
                PageRequest.of(page, entryStatSearchRequest.getPageSize(), Sort.unsorted()));

        for (EntryStatistics entryStatistic : entryStatistics) {
            boolean isYouTube = MediaType.YOUTUBE == entryStatistic.getTopicEntry().getMediaType();
            entriesStatistics.add(
                    EntryStatisticsDto.EntryStatWithEntryInfo.builder()
                            .entry(
                                    isYouTube ?
                                            topicEntryMapper.toEntryDtoFromEntityWithoutSignedUrl(entryStatistic.getTopicEntry())
                                            :topicEntryMapper.toEntryDtoFromEntity(entryStatistic.getTopicEntry())
                            )
                            .statistics(entryStatisticsMapper.toEntryStatisticsDtoFromEntity(entryStatistic))
                            .build()
            );
        }

        return EntryStatisticsDto.EntryStatSearchResponse.builder()
                .entriesStatistics(entriesStatistics)
                .pagination(Pagination.builder()
                        .totalPages(entryStatistics.getTotalPages())
                        .totalItems(entryStatistics.getTotalElements())
                        .currentPage(entryStatistics.getNumber() + 1) // index 조정
                        .pageSize(entryStatistics.getSize())
                        .build())
                .build();
    }
}

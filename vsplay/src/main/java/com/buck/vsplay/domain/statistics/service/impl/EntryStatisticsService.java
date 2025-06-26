package com.buck.vsplay.domain.statistics.service.impl;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.statistics.dto.EntryStatisticsDto;
import com.buck.vsplay.domain.statistics.entity.EntryStatistics;
import com.buck.vsplay.domain.statistics.event.EntryEvent;
import com.buck.vsplay.domain.statistics.mapper.EntryStatisticsMapper;
import com.buck.vsplay.domain.statistics.repository.EntryStatisticsRepository;
import com.buck.vsplay.domain.statistics.service.IEntryStatisticsService;
import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.entity.EntryMatch;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.entry.EntryException;
import com.buck.vsplay.domain.vstopic.exception.entry.EntryExceptionCode;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.mapper.TopicEntryMapper;
import com.buck.vsplay.domain.vstopic.moderation.TopicAccessGuard;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.global.batch.entity.BatchJobExecution;
import com.buck.vsplay.global.batch.repository.BatchJobExecutionRepository;
import com.buck.vsplay.global.constants.MediaType;
import com.buck.vsplay.global.dto.Pagination;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import com.buck.vsplay.global.util.DateTimeUtil;
import com.buck.vsplay.global.util.SortUtil;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EntryStatisticsService implements IEntryStatisticsService {

    private final EntryStatisticsRepository entryStatisticsRepository;
    private final VsTopicRepository vsTopicRepository;
    private final EntryStatisticsMapper entryStatisticsMapper;
    private final TopicEntryMapper topicEntryMapper;
    private final BatchJobExecutionRepository batchJobExecutionRepository;
    private final AuthUserService authUserService;
    private final S3Util s3Util;

    @EventListener
    public void handleEntryCrateEvent(EntryEvent.CreateEvent entryCreateEvent){
        createEntryStatistics(entryCreateEvent.getTopicEntryList());
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 클래스 트랜잭션과 분리
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

        Optional<Member> authUser = authUserService.getAuthUserOptional();
        VsTopic targetTopic = vsTopicRepository.findByIdAndDeletedFalse(topicId).orElseThrow(() ->
                new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));

        TopicAccessGuard.validateTopicAccess(targetTopic, authUser.orElse(null));

        List<EntryStatisticsDto.EntryStatWithEntryInfo> entriesStatistics = new ArrayList<>();
        int page = Math.max(entryStatSearchRequest.getPage() - 1 , 0); // index 조정

        // 정렬 기준 설정
        Sort sort = SortUtil.buildSort(Map.of(
                EntryStatistics.OrderColumn.RANK, entryStatSearchRequest.getRankOrderType()
        ), EntryStatistics.OrderColumn::getProperty);

        Page<EntryStatistics> entryStatistics = entryStatisticsRepository.findByTopicIdAndEntryNameWithTopicEntryFetch(
                topicId,
                entryStatSearchRequest.getKeyword(),
                PageRequest.of(page, entryStatSearchRequest.getPageSize(), sort));

        if (page >= entryStatistics.getTotalPages() && entryStatistics.getTotalPages() > 0) {
            entryStatistics = entryStatisticsRepository.findByTopicIdAndEntryNameWithTopicEntryFetch(
                    topicId,
                    entryStatSearchRequest.getKeyword(),
                    PageRequest.of(entryStatistics.getTotalPages() - 1, entryStatSearchRequest.getPageSize(), Sort.unsorted())
            );
        }

        for (EntryStatistics entryStatistic : entryStatistics) {
            boolean isYouTube = MediaType.YOUTUBE == entryStatistic.getTopicEntry().getMediaType();
            entriesStatistics.add(
                    EntryStatisticsDto.EntryStatWithEntryInfo.builder()
                            .entry(
                                    isYouTube ?
                                            topicEntryMapper.toEntryDtoFromEntityWithoutSignedMediaUrl(entryStatistic.getTopicEntry(), s3Util)
                                            :topicEntryMapper.toEntryDtoFromEntity(entryStatistic.getTopicEntry(), s3Util)
                            )
                            .statistics(entryStatisticsMapper.toEntryStatisticsDtoFromEntity(entryStatistic))
                            .build()
            );
        }

        // 가장 최근 랭킹 갱신 날짜 조회
        BatchJobExecution lastEntryStatsExecution =
                batchJobExecutionRepository.findCompletedEntryStatsRankingJobsOrderByEndTimeDesc()
                        .stream()
                        .findFirst()
                        .orElse(null);

        String lastUpdatedAt = (lastEntryStatsExecution != null && lastEntryStatsExecution.getEndTime() != null)
                ? DateTimeUtil.formatDateToSting(lastEntryStatsExecution.getEndTime())
                : null;

        return EntryStatisticsDto.EntryStatSearchResponse.builder()
                .entriesStatistics(entriesStatistics)
                .lastUpdatedAt(lastUpdatedAt)
                .pagination(Pagination.builder()
                        .totalPages(entryStatistics.getTotalPages())
                        .totalItems(entryStatistics.getTotalElements())
                        .currentPage(entryStatistics.getNumber() + 1) // index 조정
                        .pageSize(entryStatistics.getSize())
                        .build())
                .build();
    }

    @Override
    public EntryStatisticsDto.SingleEntryStatsResponse getSingleEntryStatistics(Long topicId, Long entryId) {
        Optional<Member> authUser = authUserService.getAuthUserOptional();
        VsTopic targetTopic = vsTopicRepository.findByIdAndDeletedFalse(topicId).orElseThrow(() ->
                new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));


        TopicAccessGuard.validateTopicAccess(targetTopic, authUser.orElse(null));

        EntryStatistics entryStatistics= entryStatisticsRepository.findByTopicEntryIdAndDeletedFalse(entryId).orElseThrow(
                () -> new EntryException(EntryExceptionCode.ENTRY_NOT_FOUND)
        );

        boolean isYoutubeMediaType = MediaType.YOUTUBE == entryStatistics.getTopicEntry().getMediaType();
        EntryDto.Entry entry = isYoutubeMediaType?
                        topicEntryMapper.toEntryDtoFromEntityWithoutSignedMediaUrl(entryStatistics.getTopicEntry(), s3Util)
                        : topicEntryMapper.toEntryDtoFromEntity(entryStatistics.getTopicEntry(), s3Util);
        EntryStatisticsDto.EntryStatistics statistics = entryStatisticsMapper.toEntryStatisticsDtoFromEntity(entryStatistics);

        return EntryStatisticsDto.SingleEntryStatsResponse.builder()
                .entry(entry)
                .statistics(statistics)
                .build();
    }
}

package com.buck.vsplay.domain.statistics.service.impl;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.statistics.dto.EntryVersusStatisticsDto;
import com.buck.vsplay.domain.statistics.entity.EntryVersusStatistics;
import com.buck.vsplay.domain.statistics.event.EntryEvent;
import com.buck.vsplay.domain.statistics.repository.EntryVersusStatisticsRepository;
import com.buck.vsplay.domain.statistics.service.IEntryVersusStatisticsService;
import com.buck.vsplay.domain.vstopic.entity.EntryMatch;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.entry.EntryException;
import com.buck.vsplay.domain.vstopic.exception.entry.EntryExceptionCode;
import com.buck.vsplay.domain.vstopic.mapper.TopicEntryMapper;
import com.buck.vsplay.domain.vstopic.moderation.TopicAccessGuard;
import com.buck.vsplay.domain.vstopic.repository.EntryRepository;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.global.constants.MediaType;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EntryVersusStatisticsService implements IEntryVersusStatisticsService {

    private final EntryVersusStatisticsRepository entryVersusStatisticsRepository;
    private final VsTopicRepository topicRepository;
    private final EntryRepository entryRepository;
    private final TopicEntryMapper topicEntryMapper;
    private final AuthUserService authUserService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 클래스 트랜잭션과 분리
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void handleEntryMatchCompletedEventForVersusStats(EntryEvent.VersusStatisticsEvent matchCompleteEvent) {
        upsertEntryVersusStatistics(matchCompleteEvent.getEntryMatch());
    }

    @Override
    public void upsertEntryVersusStatistics(EntryMatch entryMatch) {

        EntryVersusStatistics statsOfWinnerEntry = upsertStats(entryMatch.getWinnerEntry(), entryMatch.getLoserEntry(), true);
        EntryVersusStatistics statsOfLoserEntry = upsertStats(entryMatch.getLoserEntry(), entryMatch.getWinnerEntry(), false);

        entryVersusStatisticsRepository.save(statsOfWinnerEntry);
        entryVersusStatisticsRepository.save(statsOfLoserEntry);
    }

    @Override
    public EntryVersusStatisticsDto.EntryVersusStatisticsResponse getEntryVersusStatistics(Long topicId, Long entryId) {

        List<EntryVersusStatisticsDto.OpponentEntryInfoWithMatchRecord> opponentEntryInfoWithMatchRecords = new ArrayList<>();

        Optional<Member> authUser = authUserService.getAuthUserOptional();
        VsTopic targetTopic = topicRepository.findWithTournamentsByTopicId(topicId);

        TopicAccessGuard.validateTopicAccess(targetTopic, authUser.orElse(null));

        TopicEntry topicEntry = entryRepository.findWithTopicByEntryId(entryId);

        if(topicEntry == null) {
            throw new EntryException(EntryExceptionCode.ENTRY_NOT_FOUND);
        }
        if(!topicEntry.getTopic().getId().equals(topicId) ) {
            throw new EntryException(EntryExceptionCode.ENTRY_NOT_INCLUDED_IN_TOPIC);
        }

        List<EntryVersusStatistics> entryVersusStatistics = entryVersusStatisticsRepository.findByTopicEntryIdWithOpponentEntryFetch(entryId);

        if ( entryVersusStatistics != null && !entryVersusStatistics.isEmpty()){
            for (EntryVersusStatistics entryVersusStatistic : entryVersusStatistics) {
                boolean isYoutubeMediaType = MediaType.YOUTUBE == entryVersusStatistic.getOpponentEntry().getMediaType();
                opponentEntryInfoWithMatchRecords.add(
                        EntryVersusStatisticsDto.OpponentEntryInfoWithMatchRecord.builder()
                                .opponentEntry(
                                        isYoutubeMediaType ?
                                            topicEntryMapper.toEntryDtoFromEntityWithoutSignedMediaUrl(entryVersusStatistic.getOpponentEntry())
                                            : topicEntryMapper.toEntryDtoFromEntity(entryVersusStatistic.getOpponentEntry()))
                                .matchRecord(EntryVersusStatisticsDto.MatchRecord.builder()
                                        .totalMatches(entryVersusStatistic.getTotalMatches())
                                        .wins(entryVersusStatistic.getWins())
                                        .losses(entryVersusStatistic.getLosses())
                                        .winRate(entryVersusStatistic.getWinRate())
                                        .build())
                                .build()
                );
            }
        }

        return EntryVersusStatisticsDto.EntryVersusStatisticsResponse.builder()
                .matchUpRecords(opponentEntryInfoWithMatchRecords)
                .build();
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

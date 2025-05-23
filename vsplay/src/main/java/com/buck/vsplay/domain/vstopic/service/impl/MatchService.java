package com.buck.vsplay.domain.vstopic.service.impl;

import com.buck.vsplay.domain.statistics.event.EntryEvent;
import com.buck.vsplay.domain.statistics.event.TopicEvent;
import com.buck.vsplay.domain.statistics.event.TournamentEvent;
import com.buck.vsplay.domain.vstopic.dto.EntryDto;
import com.buck.vsplay.domain.vstopic.dto.EntryMatchDto;
import com.buck.vsplay.domain.vstopic.dto.TopicPlayRecordDto;
import com.buck.vsplay.domain.vstopic.entity.*;
import com.buck.vsplay.domain.vstopic.exception.entry.EntryException;
import com.buck.vsplay.domain.vstopic.exception.entry.EntryExceptionCode;
import com.buck.vsplay.domain.vstopic.exception.playrecord.PlayRecordException;
import com.buck.vsplay.domain.vstopic.exception.playrecord.PlayRecordExceptionCode;
import com.buck.vsplay.domain.vstopic.exception.tournament.TournamentException;
import com.buck.vsplay.domain.vstopic.exception.tournament.TournamentExceptionCode;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.mapper.TopicEntryMapper;
import com.buck.vsplay.domain.vstopic.repository.*;
import com.buck.vsplay.domain.vstopic.service.IMatchService;
import com.buck.vsplay.global.constants.MediaType;
import com.buck.vsplay.global.constants.PlayStatus;
import com.buck.vsplay.global.constants.TournamentStage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
 public class MatchService implements IMatchService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final VsTopicRepository vsTopicRepository;
    private final EntryRepository entryRepository;
    private final TopicPlayRecordRepository topicPlayRecordRepository;
    private final TournamentRepository tournamentRepository;
    private final EntryMatchRepository entryMatchRepository;
    private final TopicEntryMapper topicEntryMapper;

    @Override
    public TopicPlayRecordDto.PlayRecordResponse createTopicPlayRecord(Long topicId, TopicPlayRecordDto.PlayRecordRequest playRecordRequest) {
        try {
            VsTopic topic = vsTopicRepository.findByIdAndDeletedFalse(topicId).orElseThrow(
                    () -> new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));

            TopicTournament topicTournament = tournamentRepository.findByTopicIdAndTournamentStage(topicId, playRecordRequest.getTournamentStage());

            if( topicTournament == null ){
                throw new TournamentException(TournamentExceptionCode.TOURNAMENT_INVALID);
            }

            TopicPlayRecord savedTopicPlayRecord = topicPlayRecordRepository.save(TopicPlayRecord.builder()
                    .topic(topic)
                    .selectedTournament(playRecordRequest.getTournamentStage())
                    .currentTournamentStage(playRecordRequest.getTournamentStage())
                    .status(PlayStatus.IN_PROGRESS)
                    .build());

            initializeFirstTournament(savedTopicPlayRecord); // 대결 진행 기록 후 첫 대진표 생성

            applicationEventPublisher.publishEvent(new TopicEvent.PlayEvent(topic));
            applicationEventPublisher.publishEvent(new TournamentEvent.PlayEvent(topicTournament));

            return new TopicPlayRecordDto.PlayRecordResponse(savedTopicPlayRecord.getId());

        }catch (PlayRecordException e) {
            log.error("토너먼트 대진표 초기화 중 오류가 발생했습니다", e);
            throw e;
        }
    }

    @Override
    public EntryMatchDto.EntryMatchResponse getEntryMatch(Long playRecordId) {

        EntryMatchDto.EntryMatchResponse entryMatchResponse = new EntryMatchDto.EntryMatchResponse();

        TopicPlayRecord topicPlayRecord = topicPlayRecordRepository.findById(playRecordId).orElseThrow(
                () -> new PlayRecordException(PlayRecordExceptionCode.RECORD_NOT_FOUND));

        if(topicPlayRecord.getStatus().equals(PlayStatus.COMPLETED)){
            throw new PlayRecordException(PlayRecordExceptionCode.PLAY_RECORD_ALREADY_COMPLETED);
        }

        EntryMatch entryMatch = entryMatchRepository.findFirstByTopicPlayRecordOrderBySeqAsc(topicPlayRecord.getId(), topicPlayRecord.getCurrentTournamentStage());
        EntryMatch entryMatchWithEntries = entryMatchRepository.findWithEntriesById(entryMatch.getId());

        entryMatchResponse.setMatchId(entryMatchWithEntries.getId());
        entryMatchResponse.setCurrentTournament(TournamentStage.findStageNameByStage(topicPlayRecord.getCurrentTournamentStage()));
        entryMatchResponse.getEntryMatch()
                .setEntryA(mappingTopicEntryToEntryDto(entryMatchWithEntries.getEntryA()));
        entryMatchResponse.getEntryMatch()
                .setEntryB(mappingTopicEntryToEntryDto(entryMatchWithEntries.getEntryB()));

        return entryMatchResponse;
    }

    @Override
    public EntryMatchDto.UpdateEntryMatchResultResponse updateEntryMatchResult(Long playRecordId, Long matchId, EntryMatchDto.EntryMatchResultRequest entryMatchResultRequest) {

        TopicPlayRecord topicPlayRecord = topicPlayRecordRepository.findById(playRecordId).orElseThrow(
                () -> new PlayRecordException(PlayRecordExceptionCode.RECORD_NOT_FOUND));

        EntryMatch entryMatch = entryMatchRepository.findById(matchId).orElseThrow(
                () -> new PlayRecordException(PlayRecordExceptionCode.MATCH_NOT_FOUND));

        if( !entryMatch.getTopicPlayRecord().getId().equals(topicPlayRecord.getId())){ // 매치와 기록의 식별자 일치여부
            throw new PlayRecordException(PlayRecordExceptionCode.MATCH_NOT_ASSOCIATED_WITH_RECORD);
        }

        if ( entryMatch.getStatus().equals(PlayStatus.COMPLETED)){ // 이미 완료된 매치 확인
            throw new PlayRecordException(PlayRecordExceptionCode.MATCH_ALREADY_COMPLETED);
        }

        Long winnerEntryId = entryMatchResultRequest.getWinnerEntryId();
        Long loserEntryId = entryMatchResultRequest.getLoserEntryId();

        List<Long> matchEntryIds = List.of(
                entryMatch.getEntryA().getId(),
                entryMatch.getEntryB().getId()
        );

        if (!matchEntryIds.contains(winnerEntryId) || !matchEntryIds.contains(loserEntryId)) {
            throw new PlayRecordException(PlayRecordExceptionCode.INVALID_ENTRY_FOR_MATCH);
        }

        if( winnerEntryId.equals(loserEntryId)){ // 동일한 엔트리 검사
            throw new PlayRecordException(PlayRecordExceptionCode.DUPLICATE_WINNER_LOSER_ENTRY);
        }

        entryMatch.setWinnerEntry(
                entryRepository.findById(winnerEntryId).orElseThrow(
                () -> new EntryException(EntryExceptionCode.ENTRY_NOT_FOUND)));
        entryMatch.setLoserEntry(
                entryRepository.findById(loserEntryId).orElseThrow(
                () -> new EntryException(EntryExceptionCode.ENTRY_NOT_FOUND)));

        entryMatch.setStatus(PlayStatus.COMPLETED);
        entryMatchRepository.save(entryMatch);
        applicationEventPublisher.publishEvent(new EntryEvent.MatchCompleteEvent(entryMatch));
        applicationEventPublisher.publishEvent(new EntryEvent.VersusStatisticsEvent(entryMatch)); // 대결이 종료되면 상성 데이터 업데이트 ( 비동기 )

        boolean isAllTournamentStageFinish = isAllTournamentStageFinish(topicPlayRecord);

        if(isCurrentTournamentStageFinish(topicPlayRecord)){ // 진행중인 토너먼트 종료 여부 확인
            if ( isAllTournamentStageFinish){ // 현 토너먼트 완료 및 예정되어있는 모든 대진표를 완료 시
                topicPlayRecord.setStatus(PlayStatus.COMPLETED);
                topicPlayRecordRepository.save(topicPlayRecord);
                applicationEventPublisher.publishEvent(new TopicEvent.PlayCompleteEvent(topicPlayRecord.getTopic())); // 완전히 종료된 대결 횟수 갱신 이벤트 발행
            } else{ // 현 토너먼트 종료 -> 남아있는 다음 토너먼트 대진표 생성
                createNextTournamentStageEntryMatches(topicPlayRecord);
            }
        }

        return EntryMatchDto.UpdateEntryMatchResultResponse.builder()
                .message( isAllTournamentStageFinish ? "모든 대결이 완료되었습니다." : "다음 대결을 진행하세요.")
                .nextTournament(topicPlayRecord.getCurrentTournamentStage())
                .isAllMatchedCompleted(isAllTournamentStageFinish)
                .build();
    }

    /**
     * 대진표가 전혀 없는 상태에서 가장 첫 토너먼트 대진표 생성
     * @param topicPlayRecord 대결진행기록
     */
    private void initializeFirstTournament(TopicPlayRecord topicPlayRecord) throws PlayRecordException {

        List<TopicEntry> entryList = entryRepository.findByTopicId(topicPlayRecord.getTopic().getId());
        Collections.shuffle(entryList); // 무작위 순서 셔플

        entryList = entryList.subList(0, topicPlayRecord.getSelectedTournament()); // 셔플 후 토너먼트 진행에 필요한 최대 엔트리 갯수만큼 자르기

        int seq = 1;
        while (!entryList.isEmpty()) {
            TopicEntry entryA = entryList.get(0); // 순서대로 엔트리 조회
            TopicEntry entryB = entryList.get(1);
            entryMatchRepository.save(EntryMatch.builder()
                    .topicPlayRecord(topicPlayRecord)
                    .seq(seq)
                    .entryA(entryA)
                    .entryB(entryB)
                    .tournamentRound(topicPlayRecord.getSelectedTournament())
                    .status(PlayStatus.IN_PROGRESS)
                    .build());

            entryList.remove(entryA); // 매칭이 완료된 엔트리는 리스트에서 제거
            entryList.remove(entryB);
            seq++;
        }
    }

    private void createNextTournamentStageEntryMatches(TopicPlayRecord topicPlayRecord) {
        Integer currentTournamentStage = topicPlayRecord.getCurrentTournamentStage();

        if( currentTournamentStage.equals(2)) return;

        Integer nextTournamentStage = currentTournamentStage / 2;
        List<TopicEntry> winnerEntries = entryMatchRepository.findWinnerByTournamentRound(topicPlayRecord.getId(),currentTournamentStage);
        Collections.shuffle(winnerEntries); // 승리한 엔트리 셔플

        int seq = 1;
        while(!winnerEntries.isEmpty()) {
            TopicEntry entryA = winnerEntries.get(0);
            TopicEntry entryB = winnerEntries.get(1);
            entryMatchRepository.save( EntryMatch.builder()
                    .topicPlayRecord(topicPlayRecord)
                    .seq(seq)
                    .entryA(entryA)
                    .entryB(entryB)
                    .tournamentRound(nextTournamentStage)
                    .status(PlayStatus.IN_PROGRESS)
                    .build());
            winnerEntries.remove(entryA);
            winnerEntries.remove(entryB);
            seq++;
        }

        topicPlayRecord.setCurrentTournamentStage(nextTournamentStage); // 진행 스테이지 변경
        topicPlayRecordRepository.save(topicPlayRecord);
    }

    private boolean isCurrentTournamentStageFinish(TopicPlayRecord topicPlayRecord){

        List<EntryMatch> entryMatchList = entryMatchRepository.findByTopicPlayRecord(topicPlayRecord);

        for( EntryMatch entryMatch : entryMatchList){
            if( entryMatch.getStatus().equals(PlayStatus.IN_PROGRESS)){
                return false;
            }
        }
        return true;
    }

    private boolean isAllTournamentStageFinish(TopicPlayRecord topicPlayRecord){
        Integer currentTournamentStage = topicPlayRecord.getCurrentTournamentStage();

        if( !currentTournamentStage.equals(2)){ // 결승전이 아님
            return false;
        }

        EntryMatch entryMatch = entryMatchRepository.findByPlayRecordIdAndTournamentRoundOrderBySeqAsc(topicPlayRecord.getId(),currentTournamentStage);

        return entryMatch.getStatus().equals(PlayStatus.COMPLETED);
    }

    private EntryDto.Entry mappingTopicEntryToEntryDto(TopicEntry topicEntry){
        if(MediaType.YOUTUBE == topicEntry.getMediaType()){
            return topicEntryMapper.toEntryDtoFromEntityWithoutSignedMediaUrl(topicEntry);
        } else {
            return topicEntryMapper.toEntryDtoFromEntity(topicEntry);
        }
    }
}

package com.buck.vsplay.domain.vstopic.service.impl;

import com.buck.vsplay.domain.vstopic.dto.TopicPlayRecordDto;
import com.buck.vsplay.domain.vstopic.entity.EntryMatch;
import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.TopicPlayRecord;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.playrecord.PlayRecordException;
import com.buck.vsplay.domain.vstopic.exception.tournament.TournamentException;
import com.buck.vsplay.domain.vstopic.exception.tournament.TournamentExceptionCode;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.repository.*;
import com.buck.vsplay.domain.vstopic.service.IMatchService;
import com.buck.vsplay.global.constants.PlayStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
 public class MatchService implements IMatchService {

    private final VsTopicRepository vsTopicRepository;
    private final EntryRepository entryRepository;
    private final TopicPlayRecordRepository topicPlayRecordRepository;
    private final TournamentRepository tournamentRepository;
    private final EntryMatchRepository entryMatchRepository;

    @Override
    public TopicPlayRecordDto.PlayRecordResponse createTopicPlayRecord(Long topicId, TopicPlayRecordDto.PlayRecordRequest playRecordRequest) {
        try {
            VsTopic topic = vsTopicRepository.findById(topicId).orElseThrow(
                    () -> new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));

            if (!isTournamentExist(topic, playRecordRequest.getTournamentStage())){
                throw new TournamentException(TournamentExceptionCode.TOURNAMENT_INVALID);
            }

            TopicPlayRecord savedTopicPlayRecord = topicPlayRecordRepository.save(TopicPlayRecord.builder()
                    .topic(topic)
                    .selectedTournament(playRecordRequest.getTournamentStage())
                    .status(PlayStatus.IN_PROGRESS)
                    .build());

            initializeFirstTournament(savedTopicPlayRecord); // 대결 진행 기록 후 첫 대진표 생성

            return new TopicPlayRecordDto.PlayRecordResponse(savedTopicPlayRecord.getId());

        }catch (PlayRecordException e) {
            log.error("토너먼트 대진표 초기화 중 오류가 발생했습니다", e);
            throw e;
        }
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

    private boolean isTournamentExist(VsTopic vsTopic, int tournamentStage) {
        return tournamentRepository.existsByVsTopicIdAndTournamentStage(vsTopic.getId(), tournamentStage);

    }
}

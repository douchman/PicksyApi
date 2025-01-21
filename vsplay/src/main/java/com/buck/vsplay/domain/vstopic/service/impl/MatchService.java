package com.buck.vsplay.domain.vstopic.service.impl;

import com.buck.vsplay.domain.vstopic.dto.TopicPlayRecordDto;
import com.buck.vsplay.domain.vstopic.entity.TopicPlayRecord;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.vstopic.exception.tournament.TournamentException;
import com.buck.vsplay.domain.vstopic.exception.tournament.TournamentExceptionCode;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicException;
import com.buck.vsplay.domain.vstopic.exception.vstopic.VsTopicExceptionCode;
import com.buck.vsplay.domain.vstopic.repository.TopicPlayRecordRepository;
import com.buck.vsplay.domain.vstopic.repository.TournamentRepository;
import com.buck.vsplay.domain.vstopic.repository.VsTopicRepository;
import com.buck.vsplay.domain.vstopic.service.IMatchService;
import com.buck.vsplay.global.constants.PlayStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
 public class MatchService implements IMatchService {

    private final VsTopicRepository vsTopicRepository;
    private final TopicPlayRecordRepository topicPlayRecordRepository;
    private final TournamentRepository tournamentRepository;

    @Override
    public TopicPlayRecordDto.PlayRecordResponse createTopicPlayRecord(Long topicId, TopicPlayRecordDto.PlayRecordRequest playRecordRequest) {

        VsTopic topic = vsTopicRepository.findById(topicId).orElseThrow(
                () -> new VsTopicException(VsTopicExceptionCode.TOPIC_NOT_FOUND));

        if (!isTournamentExist(topic, playRecordRequest.getTournamentStage())){
            throw new TournamentException(TournamentExceptionCode.TOURNAMENT_INVALID);
        }

        TopicPlayRecord savedTopicPlayRecord = topicPlayRecordRepository.save(TopicPlayRecord.builder()
                .topic(topic)
                .tournament(playRecordRequest.getTournamentStage())
                .status(PlayStatus.IN_PROGRESS)
                .build());

        return new TopicPlayRecordDto.PlayRecordResponse(savedTopicPlayRecord.getId());
    }

    private boolean isTournamentExist(VsTopic vsTopic, int tournamentStage) {
        return tournamentRepository.existsByVsTopicIdAndTournamentStage(vsTopic.getId(), tournamentStage);

    }
}

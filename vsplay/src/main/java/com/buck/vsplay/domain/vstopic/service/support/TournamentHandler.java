package com.buck.vsplay.domain.vstopic.service.support;

import com.buck.vsplay.domain.statistics.event.TournamentEvent;
import com.buck.vsplay.domain.entry.entiity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.domain.entry.repository.EntryRepository;
import com.buck.vsplay.domain.vstopic.repository.TournamentRepository;
import com.buck.vsplay.global.constants.TournamentStage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TournamentHandler {

    private final TournamentRepository tournamentRepository;
    private final EntryRepository entryRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private static final int INITIAL_TOURNAMENT_STAGE = 2;

    public void handleTournament(VsTopic topic){

        List<TopicEntry> topicEntries = entryRepository.findByTopicIdAndDeletedFalse(topic.getId());

        if(topicEntries == null || topicEntries.isEmpty() || topicEntries.size() < INITIAL_TOURNAMENT_STAGE) {
            updateAllTournamentDeActive(topic); // 모든 토너먼트 비활성화
            return;
        }

        // 유효한 토너먼트 스테이지
        Set<Integer> validTournamentStages = calculateValidTournamentStages(topicEntries);

        updateTournament(topic, validTournamentStages);
        createTournament(topic, validTournamentStages);
    }

    private Set<Integer> calculateValidTournamentStages(List<TopicEntry> topicEntries){
        Set<Integer> validTournamentStages = new HashSet<>();

        int power = 1;
        int entryCount = topicEntries.size();

        while ( true ) { // 사용가능 토너먼트 확인
            int stage = (int)Math.pow(INITIAL_TOURNAMENT_STAGE, power++);
            if( stage > entryCount){ // 토너먼트가 보유한 엔트리 수 보다 크면 중지
                break;
            }
            validTournamentStages.add(stage);
        }

        return validTournamentStages;
    }

    // 새 토너먼트 추가
    private void createTournament(VsTopic topic, Set<Integer> validTournamentStages){
        for(Integer newStage : validTournamentStages){
            TopicTournament savedTournament = tournamentRepository.save(TopicTournament.builder()
                    .vsTopic(topic)
                    .tournamentName(TournamentStage.findStageNameByStage(newStage))
                    .tournamentStage(newStage)
                    .active(true)
                    .build());

            applicationEventPublisher.publishEvent(new TournamentEvent.CreateEvent(savedTournament)); // 커밋된 엔티티로 이벤트 발행
        }
    }

    // 기존 토너먼트 업데이트
    private void updateTournament(VsTopic topic, Set<Integer> validTournamentStages){
        // 존재하는 모든 토너먼트 조회
        List<TopicTournament> existingTournaments = tournamentRepository.findByVsTopicId(topic.getId());

        for(TopicTournament tournament : existingTournaments){
            int stage = tournament.getTournamentStage();
            boolean shouldBeActive = validTournamentStages.contains(stage);
            tournament.setActive(shouldBeActive);
            validTournamentStages.remove(stage); // 존재하는 토너먼트의 경우에는 추가 작업 필요 없음
        }
    }

    // 모든 토너먼트 비활성화
    private void updateAllTournamentDeActive(VsTopic topic){
        tournamentRepository.deActiveAllByTopicId(topic.getId());
    }
}

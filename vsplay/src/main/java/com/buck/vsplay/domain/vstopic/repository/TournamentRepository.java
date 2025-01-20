package com.buck.vsplay.domain.vstopic.repository;

import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<TopicTournament, Long> {
    boolean existsByVsTopicIdAndTournamentStage(Long topicId, Integer tournamentStage);
}

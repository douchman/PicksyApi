package com.buck.vsplay.domain.vstopic.repository;

import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TournamentRepository extends JpaRepository<TopicTournament, Long> {

    @Query("SELECT tt FROM TopicTournament tt WHERE tt.vsTopic.id = :topicId AND tt.tournamentStage = :tournamentStage")
    TopicTournament findByTopicIdAndTournamentStage(@Param("topicId") Long topicId, @Param("tournamentStage") Integer tournamentStage);
    boolean existsByVsTopicIdAndTournamentStage(Long topicId, Integer tournamentStage);
}

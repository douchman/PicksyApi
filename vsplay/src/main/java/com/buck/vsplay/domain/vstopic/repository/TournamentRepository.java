package com.buck.vsplay.domain.vstopic.repository;

import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TournamentRepository extends JpaRepository<TopicTournament, Long> {

    @Query("SELECT tt FROM TopicTournament tt WHERE tt.vsTopic.id = :topicId AND tt.tournamentStage = :tournamentStage")
    TopicTournament findByTopicIdAndTournamentStage(@Param("topicId") Long topicId, @Param("tournamentStage") Integer tournamentStage);

    List<TopicTournament> findByVsTopicIdAndActiveTrue(Long topicId);

    List<TopicTournament> findByVsTopicId(Long topicId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE TopicTournament tt SET tt.active = false WHERE tt.vsTopic.id = :topicId")
    void deActiveAllByTopicId(@Param("topicId") Long topicId);
}

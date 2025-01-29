package com.buck.vsplay.domain.statistics.repository;

import com.buck.vsplay.domain.statistics.entity.TournamentStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TournamentStatisticsRepository extends JpaRepository<TournamentStatistics, Long> {

    @Query("SELECT ts FROM TournamentStatistics ts WHERE ts.topicTournament.id = :tournamentId AND ts.tournamentStage = :tournamentStage")
    TournamentStatistics findByTournamentIdAndTournamentStage(@Param("tournamentId") Long tournamentId, @Param("tournamentStage") Integer tournamentStage);
}

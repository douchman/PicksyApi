package com.buck.vsplay.domain.statistics.repository;

import com.buck.vsplay.domain.statistics.entity.TournamentStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentStatisticsRepository extends JpaRepository<TournamentStatistics, Long> {
}

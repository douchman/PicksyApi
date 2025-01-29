package com.buck.vsplay.domain.statistics.repository;

import com.buck.vsplay.domain.statistics.entity.EntryStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryStatisticsRepository extends JpaRepository<EntryStatistics, Long> {
}

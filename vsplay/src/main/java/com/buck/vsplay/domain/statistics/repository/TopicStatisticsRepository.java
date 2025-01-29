package com.buck.vsplay.domain.statistics.repository;

import com.buck.vsplay.domain.statistics.entity.TopicStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicStatisticsRepository extends JpaRepository<TopicStatistics, Long> {
}

package com.buck.vsplay.domain.statistics.repository;

import com.buck.vsplay.domain.statistics.entity.TopicStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicStatisticsRepository extends JpaRepository<TopicStatistics, Long> {

    @Query("SELECT ts FROM TopicStatistics ts WHERE ts.vsTopic.id = :topicId")
    TopicStatistics findByVsTopic(@Param("topicId") Long topicId);
}

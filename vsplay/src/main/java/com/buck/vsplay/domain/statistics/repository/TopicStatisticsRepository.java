package com.buck.vsplay.domain.statistics.repository;

import com.buck.vsplay.domain.statistics.entity.TopicStatistics;
import com.buck.vsplay.domain.statistics.projection.MostPopularEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicStatisticsRepository extends JpaRepository<TopicStatistics, Long> {

    @Query("SELECT ts FROM TopicStatistics ts WHERE ts.vsTopic.id = :topicId")
    TopicStatistics findByVsTopic(@Param("topicId") Long topicId);


    @Query("SELECT em.winnerEntry as entry, COUNT(em) as winCount " +
            "FROM EntryMatch em " +
            "JOIN em.topicPlayRecord tpr " +
            "WHERE tpr.topic.id = :topicId " +
            "   AND em.tournamentRound = 2 " +
            "GROUP BY em.winnerEntry " +
            "ORDER BY COUNT(em) DESC")
    List<MostPopularEntry> findMostPopularEntriesByTopicId(@Param("topicId") Long topicId);
}

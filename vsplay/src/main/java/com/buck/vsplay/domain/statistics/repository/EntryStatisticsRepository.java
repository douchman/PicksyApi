package com.buck.vsplay.domain.statistics.repository;

import com.buck.vsplay.domain.statistics.entity.EntryStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryStatisticsRepository extends JpaRepository<EntryStatistics, Long> {
    @Query("SELECT es FROM EntryStatistics es WHERE es.topicEntry.id = :entryId")
    EntryStatistics findByEntryId(@Param("entryId") Long entryId);
}

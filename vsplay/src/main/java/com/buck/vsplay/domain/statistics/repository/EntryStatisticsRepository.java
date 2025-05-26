package com.buck.vsplay.domain.statistics.repository;

import com.buck.vsplay.domain.statistics.entity.EntryStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntryStatisticsRepository extends JpaRepository<EntryStatistics, Long>, JpaSpecificationExecutor<EntryStatistics> {
    @Query("SELECT es FROM EntryStatistics es WHERE es.topicEntry.id = :entryId")
    EntryStatistics findByEntryId(@Param("entryId") Long entryId);

    @Query("""
    SELECT es
    FROM EntryStatistics es
    JOIN FETCH es.topicEntry te
    WHERE te.id = :entryId
    AND te.deleted = false
    """)
    Optional<EntryStatistics> findByTopicEntryIdAndDeletedFalse(@Param("entryId") Long entryId);

    @Query("""
    SELECT es FROM EntryStatistics es
    JOIN FETCH es.topicEntry te
    WHERE te.topic.id = :topicId
    AND te.deleted = false
    AND (:entryName IS NULL OR :entryName = '' OR te.entryName LIKE CONCAT('%', :entryName, '%'))
    """)
    Page<EntryStatistics> findByTopicIdAndEntryNameWithTopicEntryFetch(@Param("topicId") Long topicId, @Param("entryName") String entryName, Pageable pageable);

    @Query("SELECT es FROM EntryStatistics es JOIN FETCH es.topicEntry te JOIN FETCH te.topic t")
    List<EntryStatistics> finalAllWithTopicEntryAndTopic();
}

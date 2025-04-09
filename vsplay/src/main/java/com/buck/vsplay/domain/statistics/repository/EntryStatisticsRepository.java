package com.buck.vsplay.domain.statistics.repository;

import com.buck.vsplay.domain.statistics.entity.EntryStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntryStatisticsRepository extends JpaRepository<EntryStatistics, Long>, JpaSpecificationExecutor<EntryStatistics> {
    @Query("SELECT es FROM EntryStatistics es WHERE es.topicEntry.id = :entryId")
    EntryStatistics findByEntryId(@Param("entryId") Long entryId);

    @EntityGraph(attributePaths = {"topicEntry"}) // for join fetch
    Page<EntryStatistics> findAll(Specification<EntryStatistics> specification, Pageable pageable);

    @Query("SELECT es FROM EntryStatistics es JOIN FETCH es.topicEntry te JOIN FETCH te.topic t")
    List<EntryStatistics> finalAllWithTopicEntryAndTopic();
}

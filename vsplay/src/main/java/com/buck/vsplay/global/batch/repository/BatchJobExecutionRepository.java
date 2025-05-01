package com.buck.vsplay.global.batch.repository;

import com.buck.vsplay.global.batch.entity.BatchJobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchJobExecutionRepository extends JpaRepository<BatchJobExecution, Long> {

    @Query("""
    SELECT bje
    FROM BatchJobExecution bje
    JOIN bje.batchJobInstance bji
    WHERE bje.status = 'COMPLETED'
    AND bji.jobName = 'entryStatsRankingJob'
    ORDER BY bje.endTime DESC
    """)
    List<BatchJobExecution> findCompletedEntryStatsRankingJobsOrderByEndTimeDesc();
}

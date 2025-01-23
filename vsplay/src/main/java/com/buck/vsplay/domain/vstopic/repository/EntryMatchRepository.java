package com.buck.vsplay.domain.vstopic.repository;

import com.buck.vsplay.domain.vstopic.entity.EntryMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface EntryMatchRepository extends JpaRepository<EntryMatch, Long> {

    @Query(value = """
        SELECT *
        FROM ENTRY_MATCH em
        WHERE em.play_record_id = :playRecordId
          AND em.status = 'IN_PROGRESS'
        ORDER BY em.seq ASC
        LIMIT 1
    """, nativeQuery = true)
    EntryMatch findFirstByTopicPlayRecordOrderBySeqAsc(@Param("playRecordId") Long playRecordId);
}

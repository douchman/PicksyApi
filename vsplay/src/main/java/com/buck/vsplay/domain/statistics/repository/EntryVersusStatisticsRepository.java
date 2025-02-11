package com.buck.vsplay.domain.statistics.repository;

import com.buck.vsplay.domain.statistics.entity.EntryVersusStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryVersusStatisticsRepository extends JpaRepository<EntryVersusStatistics, Long> {

    @Query("SELECT evs FROM EntryVersusStatistics evs WHERE evs.topicEntry.id = :entryId AND evs.opponentEntry.id = :opponentEntryId")
    EntryVersusStatistics findByEntryIdAndOpponentEntryId(@Param("entryId") Long entryId, @Param("opponentEntryId") Long opponentEntryId);

    @Query("SELECT evs FROM EntryVersusStatistics evs JOIN FETCH evs.opponentEntry oe WHERE evs.topicEntry.id = :entryId")
    List<EntryVersusStatistics> findWithOpponentEntryByEntryId(@Param("entryId") Long entryId);
}

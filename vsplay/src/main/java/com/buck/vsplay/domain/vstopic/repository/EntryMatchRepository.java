package com.buck.vsplay.domain.vstopic.repository;

import com.buck.vsplay.domain.vstopic.entity.EntryMatch;
import com.buck.vsplay.domain.entry.entiity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.TopicPlayRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EntryMatchRepository extends JpaRepository<EntryMatch, Long> {

    @Query(value = """
        SELECT *
        FROM ENTRY_MATCH em
        WHERE em.play_record_id = :playRecordId
          AND em.tournament_round = :tournamentRound
          AND em.status = 'IN_PROGRESS'
        ORDER BY em.seq
        LIMIT 1
    """, nativeQuery = true)
    EntryMatch findFirstByTopicPlayRecordOrderBySeqAsc(@Param("playRecordId") Long playRecordId, @Param("tournamentRound") Integer tournamentRound);

    @Query("SELECT em FROM EntryMatch em JOIN FETCH em.entryA JOIN FETCH em.entryB WHERE em.topicPlayRecord.id = :playRecordId AND em.tournamentRound = :tournamentRound")
    EntryMatch findByTopicPlayRecordIdAndTournamentRound(Long playRecordId, Integer tournamentRound);

    @Query("SELECT em FROM EntryMatch em JOIN FETCH em.entryA JOIN FETCH em.entryB WHERE em.id = :matchId")
    EntryMatch findWithEntriesById(Long matchId);

    List<EntryMatch> findByTopicPlayRecord(TopicPlayRecord topicPlayRecord);

    @Query("SELECT em.winnerEntry FROM EntryMatch em WHERE em.topicPlayRecord.id = :topicPlayRecordId AND em.tournamentRound = :tournamentRound")
    List<TopicEntry> findWinnerByTournamentRound(@Param("topicPlayRecordId") Long topicPlayRecordId, @Param("tournamentRound") Integer tournamentRound);

    @Query("SELECT em FROM EntryMatch em WHERE em.topicPlayRecord.id = :topicPlayRecordId AND em.tournamentRound = :tournamentRound ORDER BY em.seq")
    EntryMatch findByPlayRecordIdAndTournamentRoundOrderBySeqAsc(@Param("topicPlayRecordId") Long topicPlayRecordId, @Param("tournamentRound") Integer tournamentRound);
}

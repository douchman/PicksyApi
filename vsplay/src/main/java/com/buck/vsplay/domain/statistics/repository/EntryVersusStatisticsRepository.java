package com.buck.vsplay.domain.statistics.repository;

import com.buck.vsplay.domain.statistics.entity.EntryVersusStatistics;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryVersusStatisticsRepository extends JpaRepository<EntryVersusStatistics, Long>, JpaSpecificationExecutor<EntryVersusStatistics> {

    @Query("SELECT evs FROM EntryVersusStatistics evs WHERE evs.topicEntry.id = :entryId AND evs.opponentEntry.id = :opponentEntryId")
    EntryVersusStatistics findByEntryIdAndOpponentEntryId(@Param("entryId") Long entryId, @Param("opponentEntryId") Long opponentEntryId);

    @EntityGraph(attributePaths = {"opponentEntry"})
    default List<EntryVersusStatistics> findWithOpponentEntryBySpecification(Specification<EntryVersusStatistics> specification){
        return findAll(specification);
    }
}

package com.buck.vsplay.domain.entry.repository;

import com.buck.vsplay.domain.entry.entiity.TopicEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<TopicEntry, Long> {

    @Query("SELECT e FROM TopicEntry e WHERE e.topic.id = :topicId AND e.deleted = false AND e.moderationStatus = 'PASSED'")
    List<TopicEntry> findByTopicIdAndDeletedFalse(Long topicId);

    @Query("SELECT e FROM TopicEntry e JOIN FETCH e.topic WHERE e.id = :entryId AND e.deleted = false AND e.moderationStatus = 'PASSED'")
    TopicEntry findWithTopicByEntryId(@Param("entryId") Long entryId);

    List<TopicEntry> findByTopicIdAndIdIn(Long topicId, List<Long> ids);

    @Query("SELECT COUNT(e) FROM TopicEntry e WHERE e.topic.id = :topicId AND e.deleted = false AND e.moderationStatus = 'PASSED'")
    Long countAvailableEntriesByTopicId(@Param("topicId") Long topicId);
}

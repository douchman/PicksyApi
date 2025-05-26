package com.buck.vsplay.domain.vstopic.repository;

import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<TopicEntry, Long> {

    List<TopicEntry> findByTopicIdAndDeletedFalse(Long topicId);

    @Query("SELECT e FROM TopicEntry e JOIN FETCH e.topic WHERE e.id = :entryId AND e.deleted = false")
    TopicEntry findWithTopicByEntryId(@Param("entryId") Long entryId);

    List<TopicEntry> findByTopicIdAndIdIn(Long topicId, List<Long> ids);
}

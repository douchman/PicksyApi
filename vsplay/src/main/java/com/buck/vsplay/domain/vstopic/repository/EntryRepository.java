package com.buck.vsplay.domain.vstopic.repository;

import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<TopicEntry, Long> {

    @Query("SELECT e FROM TopicEntry e WHERE e.topic.id = :topicId")
    List<TopicEntry> findByTopicId(Long topicId);

    @Query("SELECT e FROM TopicEntry e JOIN FETCH e.topic WHERE e.id = :entryId")
    TopicEntry findWithTopicByEntryId(@Param("entryId") Long entryId);
}

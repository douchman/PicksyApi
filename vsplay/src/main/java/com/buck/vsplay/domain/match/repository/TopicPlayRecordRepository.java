package com.buck.vsplay.domain.match.repository;

import com.buck.vsplay.domain.match.entity.TopicPlayRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicPlayRecordRepository extends JpaRepository<TopicPlayRecord, Long> {
}

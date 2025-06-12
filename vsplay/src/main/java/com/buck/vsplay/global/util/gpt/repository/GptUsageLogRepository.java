package com.buck.vsplay.global.util.gpt.repository;

import com.buck.vsplay.global.util.gpt.entity.GptUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GptUsageLogRepository extends JpaRepository<GptUsageLog, Long> {
}

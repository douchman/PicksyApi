package com.buck.vsplay.domain.vstopic.repository;


import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VsTopicRepository extends JpaRepository<VsTopic, Long> {
}

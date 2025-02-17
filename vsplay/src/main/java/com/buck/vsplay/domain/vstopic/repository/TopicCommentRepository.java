package com.buck.vsplay.domain.vstopic.repository;


import com.buck.vsplay.domain.vstopic.entity.TopicComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicCommentRepository extends JpaRepository<TopicComment, Long>, JpaSpecificationExecutor<TopicComment> {
}

package com.buck.vsplay.domain.comment.repository;


import com.buck.vsplay.domain.comment.entity.TopicComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicCommentRepository extends JpaRepository<TopicComment, Long>, JpaSpecificationExecutor<TopicComment> {

    @Query("""
    SELECT c FROM TopicComment c
    WHERE c.topic.id = :topicId
    AND c.content LIKE CONCAT('%', :content, '%')
    ORDER BY c.createdAt DESC
    """)
    Page<TopicComment> findCommentByTopicIdAndContentOrderByNewest(@Param("topicId") Long topicId, @Param("content") String content, Pageable pageable);
}

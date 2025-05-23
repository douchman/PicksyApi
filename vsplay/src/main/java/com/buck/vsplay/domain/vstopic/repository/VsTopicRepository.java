package com.buck.vsplay.domain.vstopic.repository;


import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.global.constants.Visibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface VsTopicRepository extends JpaRepository<VsTopic, Long>, JpaSpecificationExecutor<VsTopic> {

    @Query("SELECT vt FROM VsTopic vt LEFT JOIN FETCH vt.tournaments WHERE vt.id = :topicId AND vt.deleted = false")
    VsTopic findWithTournamentsByTopicId(@Param("topicId") Long topicId);

    boolean existsByIdAndDeletedFalse(Long id);
    VsTopic findByIdAndDeletedFalse(Long id);

    VsTopic findWithTournamentsByShortCode(String shortCode);

    @Query("""
    SELECT vt FROM VsTopic vt
    WHERE vt.visibility = 'PUBLIC'
    AND vt.deleted = false
    AND ( :title IS NULL OR :title = '' OR vt.title LIKE CONCAT('%',:title , '%'))
    ORDER BY vt.createdAt DESC
    """)
    Page<VsTopic> findPublicTopicsByTitleOrderByNewest(@Param("title") String title, Pageable pageable);

    @Query("""
    SELECT vt FROM VsTopic vt
    JOIN TopicStatistics ts
    ON ts.vsTopic.id = vt.id
    WHERE vt.visibility = 'PUBLIC'
    AND vt.deleted = false
    AND ( :title IS NULL OR :title = '' OR vt.title LIKE CONCAT('%',:title , '%'))
    ORDER BY ts.totalMatches DESC
    """)
    Page<VsTopic> findPublicTopicsByTitleOrderByTotalMatches(@Param("title") String title, Pageable pageable);

    @Query("""
    SELECT vt FROM VsTopic vt
    WHERE vt.member.id = :memberId
    AND ( :title IS NULL OR :title = '' OR vt.title LIKE CONCAT('%',:title , '%'))
    AND ( :visibility IS NULL OR vt.visibility = :visibility)
    AND vt.deleted = false
    ORDER BY vt.createdAt DESC
    """)
    Page<VsTopic> findTopicsByMemberIdAndTitleAndVisibility(@Param("memberId") Long memberId, @Param("title") String title, @Param("visibility") Visibility visibility, Pageable pageable);
}
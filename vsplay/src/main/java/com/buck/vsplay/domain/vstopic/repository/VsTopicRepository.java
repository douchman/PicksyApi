package com.buck.vsplay.domain.vstopic.repository;


import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VsTopicRepository extends JpaRepository<VsTopic, Long> {

    @Query("SELECT vt FROM VsTopic vt WHERE vt.visibility = 'PUBLIC' AND (vt.title LIKE %:title% OR vt.subject LIKE %:subject%)")
    Page<VsTopic> findByTitleContainingAndSubjectContaining(
            @Param("title") String title,
            @Param("subject") String subject,
            Pageable pageable);

    @Query("SELECT vt FROM VsTopic vt JOIN FETCH vt.tournaments WHERE vt.id = :topicId")
    VsTopic findWithTournamentsByTopicId(@Param("topicId") Long topicId);

    @Query("SELECT vt FROM VsTopic vt WHERE vt.member.id = :memberId AND (vt.title LIKE %:title% OR vt.subject LIKE %:subject%)")
    Page<VsTopic> findByMemberIdTitleContainingAndSubjectContaining(
            @Param("memberId") Long memberId,
            @Param("title") String title,
            @Param("subject") String subject, Pageable pageable);
}

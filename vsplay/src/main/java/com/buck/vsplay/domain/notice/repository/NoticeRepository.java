package com.buck.vsplay.domain.notice.repository;

import com.buck.vsplay.domain.notice.constants.NoticeType;
import com.buck.vsplay.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>{

    @Query("""
    SELECT n
    FROM Notice n
    WHERE (:title IS NULL OR :title = '' OR n.title LIKE CONCAT('%', :title ,'%'))
    AND n.noticeType = :noticeType
    ORDER BY n.createdAt DESC
    """)
    Page<Notice> searchByTitleAndType(@Param("title") String title, @Param("noticeType") NoticeType noticeType, Pageable pageable);

}

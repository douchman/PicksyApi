package com.buck.vsplay.domain.notice.entity;

import com.buck.vsplay.domain.notice.constants.NoticeType;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "NOTICE")
public class Notice extends Timestamp {

    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "notice_type")
    @Comment("공지 유형")
    private NoticeType noticeType = NoticeType.DEFAULT;

    @Column(name = "title")
    @Comment("공지 사항 제목")
    private String title;

    @Column(name = "summary")
    @Comment("요약")
    private String summary;

    @Lob
    @Column(name = "content")
    @Comment("공지 내용")
    private String content;

}

package com.buck.vsplay.domain.inquiry.entity;

import com.buck.vsplay.domain.inquiry.constants.InquiryStatus;
import com.buck.vsplay.domain.inquiry.constants.InquiryType;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "INQUIRY_SEQ_GENERATOR", sequenceName = "INQUIRY_SEQ")
@Table(name = "INQUIRY")
@Comment("유저 문의")
public class Inquiry extends Timestamp {

    @Id
    @Column(name = "inquiry_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INQUIRY_SEQ_GENERATOR")
    private Long id;

    @Column(name = "inquiry_type")
    @Comment("문의 유형")
    private InquiryType inquiryType;

    @Column(name = "author", length = 50)
    @Comment("작성자 명")
    private String author;

    @Column(name = "email", nullable = false, length = 250)
    @Comment("이메일(답변수신)")
    private String email;

    @Column(name = "title", length = 250)
    @Comment("문의 제목")
    private String title;

    @Column(name = "content")
    @Comment("문의 내용")
    private String content;

    @Column(name = "status")
    @Comment("문의 상태( 미확인, 확인, 답변완료")
    private InquiryStatus status;

    @Column(name = "answered_at")
    @Comment("답변 일시")
    private LocalDateTime answeredAt;

    @Column(name = "ip_address", length = 45)
    @Comment("작성자 IP")
    private String ipAddress;

    @Column(name = "user_agent")
    @Comment("작성자 User-Agent")
    private String userAgent;

    @Column(name = "is_deleted")
    @Comment("삭제 여부")
    private boolean deleted;

}


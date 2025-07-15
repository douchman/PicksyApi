package com.buck.vsplay.domain.contact.entity;

import com.buck.vsplay.domain.contact.constants.UserContactStatus;
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
@SequenceGenerator(name = "CONTACT_SEQ_GENERATOR", sequenceName = "CONTACT_SEQ")
@Table(name = "USER_CONTACT")
@Comment("유저 문의")
public class UserContact extends Timestamp {

    @Id
    @Column(name = "contact_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTACT_SEQ_GENERATOR")
    private Long id;

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
    private UserContactStatus status;

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


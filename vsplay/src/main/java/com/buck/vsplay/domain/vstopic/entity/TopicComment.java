package com.buck.vsplay.domain.vstopic.entity;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "TOPIC_COMMENT")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "COMMENT_SEQ_GENERATOR" , sequenceName = "COMMENT_SEQ")
public class TopicComment extends Timestamp {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private VsTopic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean deleted;

}

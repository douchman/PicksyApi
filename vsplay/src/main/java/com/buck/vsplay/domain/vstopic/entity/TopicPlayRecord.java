package com.buck.vsplay.domain.vstopic.entity;


import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.global.constants.PlayStatus;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@Table(name = "TOPIC_PLAY_RECORD")
@SequenceGenerator(name = "TOPIC_RECORD_SEQ_GENERATOR" , sequenceName = "TOPIC_RECORD_SEQ")
@Comment("주제 플레이 기록")
public class TopicPlayRecord extends Timestamp {
    @Id
    @Column(name = "record_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOPIC_RECORD_SEQ_GENERATOR`")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private VsTopic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "tournament", nullable = false)
    private Integer tournament;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Comment("진행상태")
    private PlayStatus status = PlayStatus.IN_PROGRESS;
}

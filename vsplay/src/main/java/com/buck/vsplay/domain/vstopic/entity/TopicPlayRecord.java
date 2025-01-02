package com.buck.vsplay.domain.vstopic.entity;


import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TOPIC_PLAY_RECORD")
@SequenceGenerator(name = "TOPIC_RECORD_SEQ_GENERATOR" , sequenceName = "TOPIC_RECORD_SEQ")
public class TopicPlayRecord extends Timestamp {
    @Id
    @Column(name = "record_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOPIC_RECORD_SEQ_GENERATOR`")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private VsTopic topic;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}

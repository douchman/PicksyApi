package com.buck.vsplay.domain.vstopic.entity;


import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.global.constants.PlayStatus;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@Table(name = "TOPIC_PLAY_RECORD")
@SequenceGenerator(name = "TOPIC_RECORD_SEQ_GENERATOR" , sequenceName = "TOPIC_RECORD_SEQ")
@Comment("주제 플레이 기록")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicPlayRecord extends Timestamp {
    @Id
    @Column(name = "play_record_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOPIC_RECORD_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private VsTopic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "selected_tournament", nullable = false)
    @Comment("선택된 토너먼트")
    private Integer selectedTournament;

    @Column(name = "current_tournament_stage", nullable = false)
    @Comment("현재 진행중인 토너먼트")
    private Integer currentTournamentStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Comment("진행상태")
    private PlayStatus status = PlayStatus.IN_PROGRESS;
}

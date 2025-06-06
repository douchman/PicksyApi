package com.buck.vsplay.domain.vstopic.entity;

import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TOPIC_TOURNAMENT")
@Setter
@Getter
@NoArgsConstructor
@SequenceGenerator(name = "TOPIC_TOURNAMENT_SEQ_GENERATOR" , sequenceName = "TOPIC_TOURNAMENT_SEQ")
@Comment("대결 주제내에서 수행 가능한 토너먼트 정보")
public class TopicTournament extends Timestamp {

    @Id
    @Column(name = "tournament_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "TOPIC_TOURNAMENT_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private VsTopic vsTopic;

    @Column(name = "tournament_name", nullable = false)
    @Comment("토너먼트 이름")
    private String tournamentName;

    @Column(name = "tournament_stage", nullable = false)
    @Comment("토너먼트 ( ex. 4강 8강 16강")
    private Integer tournamentStage;

    @Column(name = "is_active")
    @Comment("사용 가능 여부")
    private boolean active;

}

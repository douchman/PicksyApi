package com.buck.vsplay.domain.statistics.entity;


import com.buck.vsplay.domain.vstopic.entity.TopicTournament;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TOURNAMENT_STATISTICS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "TOURNAMENT_STATS_SEQ_GENERATOR" , sequenceName = "TOURNAMENT_STATS_SEQ")
@Comment("토너먼트 분석 데이터")
@Builder
public class TournamentStatistics extends Timestamp {

    @Id
    @Column(name = "tournament_stats_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOURNAMENT_STATS_SEQ_GENERATOR")
    private Long tournamentStatsId;

    @OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name ="tournament_id")
    private TopicTournament topicTournament;

    @Column(name = "tournament_stage", unique = true)
    @Comment("토너먼트 단계 (ex. 8강 16강")
    private Integer tournamentStage;

    @Column(name = "stage_matches")
    @Comment("진행 횟수")
    @Builder.Default
    private Integer stageMatches = 0;

    @Column(name = "is_active", nullable = false)
    @Comment("활성화 여부")
    @Builder.Default
    private boolean active = true;

}

package com.buck.vsplay.domain.statistics.entity;


import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TOURNAMENT_STATISTICS")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "TOURNAMENT_STATS_SEQ_GENERATOR" , sequenceName = "TOURNAMENT_STATS_SEQ")
@Comment("토너먼트 분석 데이터")
public class TournamentStatistics extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOURNAMENT_STATS_SEQ_GENERATOR")
    private Long tournamentStatsId;

    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name ="topic_id")
    private VsTopic vsTopic;

    @Column(name = "tournament_stage", unique = true)
    @Comment("토너먼트 단계 (ex. 8강 16강")
    private Integer tournamentStage;

    @Column(name = "stage_matches")
    @Comment("진행 횟수")
    private Integer stageMatches;

    @Column(name = "is_active", nullable = false)
    @Comment("활성화 여부")
    private boolean active;

}

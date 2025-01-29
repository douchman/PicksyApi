package com.buck.vsplay.domain.statistics.entity;


import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "ENTRY_STATISTICS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "ENTRY_STATS_SEQ_GENERATOR" , sequenceName = "ENTRY_STATS_SEQ")
@Comment("엔트리 분석 데이터(대결횟수, 승리/패배 등의 기록)")
@Builder
public class EntryStatistics extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENTRY_STATS_SEQ_GENERATOR")
    @Column(name = "entry_stats_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "entry_id")
    private TopicEntry topicEntry;

    @Column(name = "total_matches")
    private Integer totalMatches;

    @Column(name = "total_wins")
    private Integer totalWins;

    @Column(name = "total_losses")
    private Integer totalLosses;

    @Column(name = "win_rate")
    private Double winRate;

    @Column(name = "highest_tournament")
    @Comment("가장 높이 올라간 토너먼트")
    private Integer highestTournament;

}

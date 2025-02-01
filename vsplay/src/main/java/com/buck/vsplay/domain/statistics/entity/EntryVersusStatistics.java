package com.buck.vsplay.domain.statistics.entity;

import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "ENTRY_VERSUS_STATISTICS")
@Getter
@Setter
@SequenceGenerator(name = "VERSUS_STATS_SEQ_GENERATOR" , sequenceName = "VERSUS_STATS_SEQ")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Comment("엔트리 상성 통계")
public class EntryVersusStatistics extends Timestamp {

    @Id
    @Column(name = "versus_stats_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VERSUS_STATS_SEQ_GENERATOR" )
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_id", nullable = false)
    private TopicEntry topicEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_entry_id", nullable = false)
    private TopicEntry opponentEntry;

    @Column(name = "total_matches")
    private Integer totalMatches;

    @Column(name = "wins")
    private Integer wins;

    @Column(name = "losses")
    private Integer losses;

    @Column(name = "win_rate")
    private Double winRate;

    public void increaseTotalMatches() {
        this.totalMatches++;
    }

    public void increaseWins() {
        this.wins++;
    }

    public void increaseLosses() {
        this.losses++;
    }


    public void calculateWinRate() {
        this.winRate = (totalMatches.equals(0) ? 0.0 : (double) this.wins / this.totalMatches) * 100;
    }
}

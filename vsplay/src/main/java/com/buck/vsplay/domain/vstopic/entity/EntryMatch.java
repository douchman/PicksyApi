package com.buck.vsplay.domain.vstopic.entity;

import com.buck.vsplay.domain.entry.entiity.TopicEntry;
import com.buck.vsplay.global.constants.PlayStatus;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "ENTRY_MATCH")
@SequenceGenerator(name = "ENTRY_MATCH_SEQ_GENERATOR", sequenceName = "ENTRY_MATCH_SEQ")
@Comment("엔트리 간 대결 기록")
@AllArgsConstructor
@Builder
public class EntryMatch extends Timestamp {
    @Id
    @Column(name = "match_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENTRY_MATCH_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "play_record_id", nullable = false)
    private TopicPlayRecord topicPlayRecord;

    @Column(name = "seq", nullable = false)
    @Comment("순서")
    private Integer seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_a", nullable = false)
    private TopicEntry entryA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_b", nullable = false)
    private TopicEntry entryB;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_entry")
    @Comment("승리한 엔트리")
    private TopicEntry winnerEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loser_entry")
    @Comment("패배한 엔트리")
    private TopicEntry loserEntry;

    @Column(name = "tournament_round", nullable = false)
    @Comment("토너먼트")
    private Integer tournamentRound;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Comment("진행상태")
    private PlayStatus status = PlayStatus.IN_PROGRESS;
}

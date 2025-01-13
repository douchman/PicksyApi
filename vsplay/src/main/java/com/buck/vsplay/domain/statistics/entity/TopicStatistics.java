package com.buck.vsplay.domain.statistics.entity;


import com.buck.vsplay.domain.vstopic.entity.TopicEntry;
import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "TOPIC_STATISTICS")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "TOPIC_STATS_SEQ_GENERATOR" , sequenceName = "TOPIC_STATS_SEQ")
@Comment("주제 분석 데이터")
public class TopicStatistics extends Timestamp {

    @Id
    @Column(name = "topic_stats_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOPIC_STATS_SEQ_GENERATOR")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "topic_id" , nullable = false)
    private VsTopic vsTopic;

    @Column(name = "total_matches")
    @Comment("진행한 총 대결 횟수(중복포함")
    private Integer totalMatches;

    @Column(name = "total_players")
    @Comment("진행한 총 사용자 수(중복제외)")
    private Integer totalPlayers;

    @Column(name = "completed_matches")
    @Comment("완전히 완료된 대결 횟수")
    private Integer completedMatches;

    @Column(name = "completed_players")
    @Comment("완전히 완료한 고유 사용자 수")
    private Integer completedPlayers;

    @OneToOne
    @JoinColumn(name = "most_popular_entry_id")
    @Comment("가장 인기있는 엔트리")
    private TopicEntry mostPopularEntry;

    @Column(name = "first_played_at")
    @Comment("최초 진행 날짜")
    private LocalDateTime firstPlayedAt;

    @Column(name = "last_played_at")
    @Comment("가장 최근 진행 날짜")
    private LocalDateTime lastPlayedAt;
}

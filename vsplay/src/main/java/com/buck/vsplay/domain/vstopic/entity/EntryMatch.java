package com.buck.vsplay.domain.vstopic.entity;

import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "ENTRY_MATCH")
@SequenceGenerator(name = "ENTRY_MATCH_SEQ_GENERATOR", sequenceName = "ENTRY_MATCH_SEQ")
@Comment("엔트리 간 대결 기록, 엔트리 1:1 이므로 하나의 엔트리 대결당 2개의 entry_match 가 기록됨")
public class EntryMatch extends Timestamp {
    @Id
    @Column(name = "match_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_id")
    private TopicEntry topicEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_entry_id")
    @Comment("상대 엔트리")
    private TopicEntry opponentEntry;

    @Column(name = "is_winner")
    private boolean winner = false;

}

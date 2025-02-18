package com.buck.vsplay.domain.vstopic.entity;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.global.constants.Visibility;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "VS_TOPIC")
@SequenceGenerator(name = "TOPIC_SEQ_GENERATOR", sequenceName = "TOPIC_SEQ")
public class VsTopic extends Timestamp{

    @Id
    @Column(name = "topic_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOPIC_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "title", nullable = false, length = 25)
    @Comment("대결 제목")
    private String title;

    @Column(name = "subject", nullable = false, length = 50)
    @Comment("대결 주제")
    private String subject;

    @Column(name = "description", length = 200)
    @Comment("대결 설명")
    private String description;

    @Column(name = "thumbnail", length = 200)
    @Comment("대표이미지")
    private String thumbnail;

    @Column(name = "is_delete")
    private boolean deleted = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    @ColumnDefault("'PUBLIC'")
    @Comment("대결 공개 범위")
    private Visibility visibility = Visibility.PUBLIC;

    @Column(name = "short_code", unique = true, length = 100)
    @Comment("비공개 링크")
    private String shortCode;

    @OneToMany(mappedBy = "vsTopic", fetch = FetchType.LAZY)
    private List<TopicTournament> tournaments;
}

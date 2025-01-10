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
@Table(name = "TOPIC_ENTRY")
@SequenceGenerator(name ="ENTRY_SEQ_GENERATOR", sequenceName = "ENTRY_SEQ")
@Comment("주제에 소속되는 대결 아이템(엔트리)")
public class TopicEntry extends Timestamp {

    @Id
    @Column(name = "entry_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENTRY_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private VsTopic topic;

    @Column(name = "entry_name", nullable = false, length = 30)
    @Comment("엔트리 이름")
    private String entryName;

    @Column(name = "description", nullable = false, length = 200)
    @Comment("엔트리 설명")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false)
    @Comment("엔트리 미디어 타입 (사진, 영상링크 등 )")
    private MediaType mediaType;

    public enum MediaType{
        IMAGE, VIDEO
    }

}

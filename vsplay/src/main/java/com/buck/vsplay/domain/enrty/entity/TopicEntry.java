package com.buck.vsplay.domain.enrty.entity;


import com.buck.vsplay.domain.vstopic.entity.VsTopic;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "TOPIC_ENTRY")
@SequenceGenerator(name ="ENTRY_SEQ_GENERATOR", sequenceName = "ENTRY_SEQ")
public class TopicEntry extends Timestamp {

    @Id
    @Column(name = "entry_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENTRY_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    @ToString.Exclude
    private VsTopic topic;

    @Column(name = "entry_name", nullable = false, length = 30)
    private String entryName;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false)
    private MediaType mediaType;

    public enum MediaType{
        IMAGE, VIDEO;
    }

}

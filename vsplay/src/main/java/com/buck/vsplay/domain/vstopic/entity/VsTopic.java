package com.buck.vsplay.domain.vstopic.entity;

import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "VS_TOPIC")
@SequenceGenerator(name = "TOPIC_SEQ_GENERATOR", sequenceName = "TOPIC_SEQ")
public class VsTopic extends Timestamp{

    @Id
    @Column(name = "topic_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOPIC_SEQ_GENERATOR")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "subject")
    private String subject;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    @ColumnDefault("'PUBLIC'")
    private Visibility visibility = Visibility.PUBLIC;

    public enum Visibility{
        PUBLIC,
        PRIVATE,
        FRIEND_ONLY
    }
}

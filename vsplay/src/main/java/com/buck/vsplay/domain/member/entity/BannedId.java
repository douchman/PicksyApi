package com.buck.vsplay.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "BANNED_ID")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BannedId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increase
    @Column(name = "banned_id")
    private Long id;

    @Column(name = "word", unique = true, nullable = false)
    @Comment("금칙어")
    private String word;

    @Column(name = "reason")
    @Comment("사유")
    private String reason;

}

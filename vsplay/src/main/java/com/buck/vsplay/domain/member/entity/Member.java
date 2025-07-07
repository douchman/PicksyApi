package com.buck.vsplay.domain.member.entity;

import com.buck.vsplay.domain.member.role.Role;
import com.buck.vsplay.global.entity.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;


@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "password")
@SequenceGenerator( name = "MEMBER_SEQ_GENERATOR" , sequenceName = "MEMBER_SEQ")
@Table(name = "MEMBER")
@Builder
@AllArgsConstructor
public class Member extends Timestamp {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    private Long id;

    @Column(name = "login_id", unique = true, nullable = false)
    @Comment("회원 로그인 아이디")
    private String loginId;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}

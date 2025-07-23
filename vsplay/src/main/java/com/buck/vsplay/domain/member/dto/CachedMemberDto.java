package com.buck.vsplay.domain.member.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CachedMemberDto {
    private Long id;
    private String loginId;
    private String memberName;
}

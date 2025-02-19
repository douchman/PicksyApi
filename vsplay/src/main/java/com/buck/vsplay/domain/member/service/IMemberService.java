package com.buck.vsplay.domain.member.service;

import com.buck.vsplay.domain.member.dto.MemberDto;

public interface IMemberService {
    MemberDto.MemberInfo getMemberInfo();
    void createMember(MemberDto.CreateMemberRequest createMemberRequest);
    void updateMember(Long memberId, MemberDto.UpdateMemberRequest updateMemberRequest);
}

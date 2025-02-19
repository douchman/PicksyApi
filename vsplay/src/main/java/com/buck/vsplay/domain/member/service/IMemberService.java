package com.buck.vsplay.domain.member.service;

import com.buck.vsplay.domain.member.dto.MemberDto;
import com.buck.vsplay.domain.member.entity.Member;

import java.util.List;

public interface IMemberService {
    List<Member> getMemberList();
    void createMember(MemberDto.CreateMemberRequest createMemberRequest);
    void updateMember(Long memberId, MemberDto.UpdateMemberRequest updateMemberRequest);
    void deleteMember(Long id);
}

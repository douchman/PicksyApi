package com.buck.vsplay.domain.member.service;

import com.buck.vsplay.domain.member.dto.MemberDto;
import com.buck.vsplay.domain.member.entity.Member;

import java.util.List;

public interface IMemberService {
    List<Member> getMemberList();
    void registerMember(MemberDto.MemberInfo member);
    void updateMember(MemberDto.MemberInfo member);
    void deleteMember(Long id);
}

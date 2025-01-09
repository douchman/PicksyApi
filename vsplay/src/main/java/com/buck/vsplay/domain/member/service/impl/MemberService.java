package com.buck.vsplay.domain.member.service.impl;

import com.buck.vsplay.domain.member.dto.MemberDto;
import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.member.exception.MemberException;
import com.buck.vsplay.domain.member.exception.MemberExceptionCode;
import com.buck.vsplay.domain.member.mapper.MemberMapper;
import com.buck.vsplay.domain.member.repository.MemberRepository;
import com.buck.vsplay.domain.member.role.Role;
import com.buck.vsplay.domain.member.service.IMemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService implements IMemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public List<Member> getMemberList() {
        return memberRepository.findAll();
    }

    @Override
    public void registerMember(MemberDto.MemberInfo member) {
        log.info("Registering member: {}", member);

        if (memberRepository.findByLoginId(member.getLoginId()).isPresent()) {
            throw new MemberException(MemberExceptionCode.MEMBER_DUPLICATE_ID);
        }

        Member registerMember = memberMapper.toEntity(member);
        registerMember.setPassword(passwordEncoder.encode(member.getPassword()));
        registerMember.setRole(Role.GENERAL);

        memberRepository.save(registerMember);
    }

    @Override
    public void updateMember(MemberDto.MemberInfo member) {
        Member existingMember = memberRepository.findById(member.getId()).orElseThrow(() -> new RuntimeException("일치하는 회원을 찾을 수 없습니다."));

        existingMember.setMemberName(member.getMemberName());
        log.info("member info update success");
    }

    @Override
    public void deleteMember(Long id) {
        Member existingMember = memberRepository.findById(id).orElseThrow( () -> new RuntimeException("일치하는 회원을 찾을 수 없습니다."));
        memberRepository.delete(existingMember);
        log.info("member delete success");
    }
}

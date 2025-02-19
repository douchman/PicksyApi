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

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService implements IMemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public void createMember(MemberDto.CreateMemberRequest createMemberRequest) {
        if (memberRepository.findByLoginId(createMemberRequest.getLoginId()).isPresent()) {
            throw new MemberException(MemberExceptionCode.MEMBER_DUPLICATE_ID);
        }

        Member registerMember = memberMapper.toEntityFromCreateMemberDto(createMemberRequest);
        registerMember.setPassword(passwordEncoder.encode(createMemberRequest.getPassword()));
        registerMember.setRole(Role.GENERAL);

        memberRepository.save(registerMember);
    }

    @Override
    public void updateMember(Long memberId, MemberDto.UpdateMemberRequest updateMemberRequest) {
        Member existingMember = memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException("일치하는 회원을 찾을 수 없습니다."));

        existingMember.setMemberName(updateMemberRequest.getMemberName());
        log.info("member info update success");
    }
}

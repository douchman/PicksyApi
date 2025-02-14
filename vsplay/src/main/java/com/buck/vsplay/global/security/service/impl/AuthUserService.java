package com.buck.vsplay.global.security.service.impl;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.member.repository.MemberRepository;
import com.buck.vsplay.global.security.service.IAuthUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserService implements IAuthUserService {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> getAuthUserOptional() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없거나 principal이 Long 타입이 아니면 비로그인
        if (authentication == null || !(authentication.getPrincipal() instanceof Long memberId)) {
            return Optional.empty();
        }

        // DB에서 사용자 정보 조회 후 반환
        return memberRepository.findById(memberId);
    }

    @Override
    public Member getAuthUser() {
        return getAuthUserOptional()
                .orElseThrow(() -> new IllegalStateException("유저 정보를 확인할 수 없습니다."));
    }
}
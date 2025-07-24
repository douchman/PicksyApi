package com.buck.vsplay.global.security.service.impl;

import com.buck.vsplay.domain.member.dto.CachedMemberDto;
import com.buck.vsplay.global.security.service.IAuthUserService;
import com.buck.vsplay.global.security.user.CustomUserDetail;
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

    @Override
    public Optional<CachedMemberDto> getCachedMemberOptional() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없거나 principal 이 CustomUserDetail 가 아니면 비로그인
        if(authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetail userDetail )) {
            return Optional.empty();
        }

        return Optional.of(
                new CachedMemberDto(userDetail.getId(), userDetail.getUsername())
        );
    }

    @Override
    public CachedMemberDto getCachedMember() {
        return getCachedMemberOptional().orElseThrow(() -> new IllegalStateException("유저 정보를 확인할 수 없습니다."));
    }
}
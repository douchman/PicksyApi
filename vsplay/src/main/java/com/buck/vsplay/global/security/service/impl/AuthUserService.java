package com.buck.vsplay.global.security.service.impl;

import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.member.repository.MemberRepository;
import com.buck.vsplay.global.security.user.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthUserService implements IAuthUserService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("일치하는 회원을 찾을 수 없습니다."));
        return new CustomUserDetail(findMember);
    }

}

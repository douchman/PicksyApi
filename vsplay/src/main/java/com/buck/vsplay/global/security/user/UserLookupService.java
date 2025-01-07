package com.buck.vsplay.global.security.user;


import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.member.exception.MemberException;
import com.buck.vsplay.domain.member.exception.MemberExceptionCode;
import com.buck.vsplay.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLookupService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByLoginId(username)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.MEMBER_NOT_FOUND));
        return new CustomUserDetail(findMember);
    }
}

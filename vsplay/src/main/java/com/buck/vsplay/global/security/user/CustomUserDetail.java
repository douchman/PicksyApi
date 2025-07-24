package com.buck.vsplay.global.security.user;

import com.buck.vsplay.domain.member.dto.CachedMemberDto;
import com.buck.vsplay.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;



@Getter
@Setter
public class CustomUserDetail implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetail(Member member){
        this.id = member.getId();
        this.username = member.getLoginId();
        this.password = member.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority(member.getRole().getRoleName()));
    }

    public CustomUserDetail(CachedMemberDto cachedMemberDto, Collection<? extends GrantedAuthority> authorities) {
        this.id = cachedMemberDto.getId();
        this.username = cachedMemberDto.getLoginId();
        this.password = null;
        this.authorities = List.of(new SimpleGrantedAuthority(authorities.toString()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}

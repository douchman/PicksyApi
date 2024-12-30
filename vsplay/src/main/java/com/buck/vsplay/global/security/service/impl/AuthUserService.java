package com.buck.vsplay.global.security.service.impl;

import com.buck.vsplay.global.security.service.IAuthUserService;
import com.buck.vsplay.global.security.user.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthUserService implements IAuthUserService {

    @Override
    public String getAuthUserLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetail customUserDetail)) {
            throw new IllegalStateException("Authentication is invalid or not set");
        }
        return customUserDetail.getUsername();
    }

    @Override
    public Long getAuthUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetail customUserDetail)) {
            throw new IllegalStateException("JWT Token is missing in the authentication context");
        }
        return customUserDetail.getId();
    }
}

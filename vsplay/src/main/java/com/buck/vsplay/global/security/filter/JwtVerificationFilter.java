package com.buck.vsplay.global.security.filter;

import com.buck.vsplay.global.security.jwt.JwtService;
import com.buck.vsplay.global.security.jwt.exception.JwtException;
import com.buck.vsplay.global.security.jwt.exception.JwtExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtExceptionHandler jwtExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        // get jwt from cookie
        String token = getJwtFromCookie(request);

        if( token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            if (jwtService.validateToken(token)) {
                Authentication authentication = jwtService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            }
        } catch (JwtException e){
            jwtExceptionHandler.handleJwtException(response, e);
        }

    }

    private String getJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}

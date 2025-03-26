package com.buck.vsplay.global.security.filter;

import com.buck.vsplay.global.security.configuration.PublicPaths;
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
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
         
        // 🔹 로그인 상태 확인 API는 필터 실행 (JWT 검증 필요)
        if ("/member/auth".equals(requestUri) && "GET".equalsIgnoreCase(method)) {
            return false; // ✅ JWT 필터 실행
        }

        return PublicPaths.isPublicEndPoint(requestUri, method);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // get jwt from cookie
        String token = getJwtFromCookie(request);

        if ( token != null){
            try {
                if (jwtService.validateToken(token)) {
                    Authentication authentication = jwtService.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException e){
                jwtExceptionHandler.handleJwtException(response, e);
                return ; // 예외 시 다음 필터 실행 방지
            }
        }
        filterChain.doFilter(request, response);
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

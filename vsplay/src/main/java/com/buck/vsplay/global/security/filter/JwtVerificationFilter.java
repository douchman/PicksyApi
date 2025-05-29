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

        /*
         * - public 요청은 허용되지만, 내부 비즈니스 로직에서 SecurityContext 참조가 필요한 경로
         * - 즉, 인증이 필수는 아니지만, 인증 정보(JWT)가 있다면 활용할 수 있도록
         *   JwtVerificationFilter를 실행해 SecurityContext를 설정해야 하는 대상
         */
        if (PublicPaths.isOptionalAuthEndPoint(requestUri, method)) {
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

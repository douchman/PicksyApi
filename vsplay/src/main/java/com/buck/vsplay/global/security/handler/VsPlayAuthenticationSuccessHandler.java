package com.buck.vsplay.global.security.handler;

import com.buck.vsplay.global.security.dto.AuthenticationDto;
import com.buck.vsplay.global.security.jwt.JwtService;
import com.buck.vsplay.global.security.user.CustomUserDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
public class VsPlayAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    @Value("${app.cookie.domain:#{null}}")
    String cookieDomain;

    private final JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        String token = jwtService.generateAccessToken(userDetail);

        ResponseCookie jwtCookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .domain(cookieDomain)
                .sameSite("Lax")
                .maxAge(3600)
                .build();


        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new AuthenticationDto.SuccessResponse()));
    }
}

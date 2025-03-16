package com.buck.vsplay.global.security.filter;

import com.buck.vsplay.global.security.dto.AuthenticationDto;
import com.buck.vsplay.global.security.exception.SecurityException;
import com.buck.vsplay.global.security.exception.SecurityExceptionCode;
import com.buck.vsplay.global.security.jwt.JwtService;
import com.buck.vsplay.global.security.user.CustomUserDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper 추가

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken;

        try {
            if( request.getContentType() != null && request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)){
                // JSON 요청 처리
                AuthenticationDto.LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), AuthenticationDto.LoginRequest.class);
                authToken = new UsernamePasswordAuthenticationToken(loginRequest.getId(), loginRequest.getPassword());
            } else {
                authToken = new UsernamePasswordAuthenticationToken(request.getParameter("id"), request.getParameter("password"));
            }
        } catch (IOException e) {
            throw new SecurityException(SecurityExceptionCode.SECURITY_ERROR);
        }
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        CustomUserDetail userDetail = (CustomUserDetail) authResult.getPrincipal();

        log.info(" successfulAuthentication userDetail => {} ", userDetail.toString());
        String token = jwtService.generateAccessToken(userDetail);

        response.setHeader("Authorization", "Bearer " + token);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new AuthenticationDto.SuccessResponse(token)));

        super.successfulAuthentication(request, response, chain, authResult);
    }

}


package com.buck.vsplay.global.security.jwt.exception;


import com.buck.vsplay.global.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtExceptionHandler {

    public void handleJwtException(HttpServletResponse response, JwtException jwtException) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponse jwtErrorResponse = ErrorResponse.of(jwtException.getBaseExceptionCode());

            response.setStatus(jwtException.getBaseExceptionCode().getStatus());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(jwtErrorResponse));
        } catch (IOException ioException) {
            log.error("예외 응답 작성 중 오류 발생: {}", ioException.getMessage());
        }
    }
}

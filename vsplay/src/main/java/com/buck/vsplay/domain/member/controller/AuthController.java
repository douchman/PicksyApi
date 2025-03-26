package com.buck.vsplay.domain.member.controller;

import com.buck.vsplay.domain.member.dto.AuthCheckDto;
import com.buck.vsplay.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("member")
@RequiredArgsConstructor
public class AuthController {
    @GetMapping("auth")
    public ResponseEntity<SingleResponseDto<AuthCheckDto.AuthCheckResponse>> checkAuthStatus(Authentication authentication) {
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), AuthCheckDto.AuthCheckResponse.builder().isAuth(isAuthenticated).build()), HttpStatus.OK);
    }
}

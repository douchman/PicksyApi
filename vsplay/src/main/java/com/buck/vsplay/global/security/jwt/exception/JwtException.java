package com.buck.vsplay.global.security.jwt.exception;

import com.buck.vsplay.global.exception.BaseException;

public class JwtException extends BaseException{
    public JwtException(JwtExceptionCode jwtExceptionCode) {
        super(jwtExceptionCode);
    }
}
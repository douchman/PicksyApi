package com.buck.vsplay.global.security.jwt.exception;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum JwtExceptionCode implements BaseExceptionCode {
    EXPIRED_TOKEN(401, "만료된 토큰입니다", "JWT_001"),
    UNSUPPORTED_TOKEN(400, "지원되지 않는 JWT 형식입니다.", "JWT_002"),
    MALFORMED_TOKEN(400, "JWT 형식이 올바르지 않습니다.", "JWT_003"),
    INVALID_SIGNATURE(401, "JWT 서명이 유효하지 않습니다.", "JWT_004"),
    ILLEGAL_ARGUMENT(400, "JWT 입력값이 올바르지 않습니다.", "JWT_005"),
    INVALID_ISSUER(401, "JWT 발급자가 유효하지 않습니다.", "JWT_006");



    private final int status;
    private final String message;
    private final String errorCode;

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    JwtExceptionCode(Integer status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}

package com.buck.vsplay.global.security.exception;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum SecurityExceptionCode implements BaseExceptionCode {
    SECURITY_ERROR(500, "로그인 처리 중 오류가 발생했습니다.", "SECURITY_ERROR");


    private final Integer status;
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

    SecurityExceptionCode(Integer status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}

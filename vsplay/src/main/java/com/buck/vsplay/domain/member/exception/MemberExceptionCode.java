package com.buck.vsplay.domain.member.exception;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum MemberExceptionCode implements BaseExceptionCode {

    MEMBER_NOT_FOUND(401, "일치하는 회원을 찾을 수 없습니다.", "MEMBER_001");


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

    MemberExceptionCode(int status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}

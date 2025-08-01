package com.buck.vsplay.domain.member.exception;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum MemberExceptionCode implements BaseExceptionCode {

    MEMBER_NOT_FOUND(404, "일치하는 회원을 찾을 수 없습니다.", "MEMBER_NOT_FOUND"),
    MEMBER_DUPLICATE_ID (409, "이미 존재하는 아이디 입니다.", "MEMBER_DUPLICATE_ID"),
    BANNED_ID_USED (400, "사용할 수 없는 아이디입니다.", "BANNED_ID_USED"),
    BANNED_NICKNAME_USED (400, "사용할 수 없는 닉네임입니다.", "BANNED_NICKNAME_USED"),
    INVALID_ID_FORMAT (400, "아이디는 영어(소문자), 숫자를 자유롭게 조합해 4 ~ 20자까지 입력할 수 있어요.", "INVALID_ID_FORMAT"),
    INVALID_NICKNAME_FORMAT (400, "닉네임은 영어(소문자), 한글, 숫자를 자유롭게 조합해 12자까지 입력할 수 있어요.", "INVALID_NICKNAME_FORMAT"),
    INVALID_PASSWORD_FORMAT (400, "비밀번호는 영어, 숫자, 특수문자 중 자유롭게 입력할 수 있어요.(4~20자).", "INVALID_PASSWORD_FORMAT"),
    ;

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

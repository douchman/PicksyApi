package com.buck.vsplay.domain.notice.exception;

import com.buck.vsplay.global.exception.BaseExceptionCode;

public enum NoticeExceptionCode implements BaseExceptionCode {
    NOTICE_NOT_EXISTS(404, "존재하지 않는 공지사항 입니다.", "NOTICE_NOT_EXISTS");

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

    NoticeExceptionCode(int status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}

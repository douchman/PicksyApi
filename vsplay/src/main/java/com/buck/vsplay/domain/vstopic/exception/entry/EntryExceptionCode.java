package com.buck.vsplay.domain.vstopic.exception.entry;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum EntryExceptionCode implements BaseExceptionCode {
    ;


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

    EntryExceptionCode(Integer status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}

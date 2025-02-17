package com.buck.vsplay.domain.vstopic.exception.entry;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum EntryExceptionCode implements BaseExceptionCode {
    ENTRY_NOT_FOUND(404, "존재하지 않는 엔트리 입니다.", "ENTRY_001"),
    ENTRY_NOT_INCLUDED_IN_TOPIC(400, "대결주제에 포함되지 않는 엔트리 입니다.", "ENTRY_002");


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

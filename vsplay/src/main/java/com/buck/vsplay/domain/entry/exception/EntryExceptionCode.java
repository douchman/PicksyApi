package com.buck.vsplay.domain.entry.exception;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum EntryExceptionCode implements BaseExceptionCode {
    ENTRY_NOT_FOUND(404, "존재하지 않는 엔트리 입니다.", "ENTRY_NOT_FOUND"),
    BAD_WORD_DETECTED(422, "입력한 내용 중 비속어가 포함되어 있습니다.", "BAD_WORD_DETECTED"),
    ENTRY_NOT_INCLUDED_IN_TOPIC(400, "대결주제에 포함되지 않는 엔트리 입니다.", "ENTRY_NOT_INCLUDED_IN_TOPIC");


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

package com.buck.vsplay.domain.vstopic.exception.vstopic;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum VsTopicExceptionCode implements BaseExceptionCode {
    TOPIC_NOT_FOUND(404, "존재하지 않는 대결주제 입니다.", "TOPIC_001");


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

    VsTopicExceptionCode(Integer status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}

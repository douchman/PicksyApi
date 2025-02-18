package com.buck.vsplay.domain.vstopic.exception.vstopic;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum VsTopicExceptionCode implements BaseExceptionCode {
    TOPIC_NOT_FOUND(404, "존재하지 않는 대결주제 입니다.", "TOPIC_001"),
    TOPIC_NOT_PUBLIC(403, "비공개 또는 접근이 제한된 대결 주제입니다.", "TOPIC_002"),
    TOPIC_NOT_UNLISTED(400, "해당 대결주제는 비공개 링크로 공유할 수 없습니다.", "TOPIC_003");


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

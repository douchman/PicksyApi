package com.buck.vsplay.domain.vstopic.exception.vstopic;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum VsTopicExceptionCode implements BaseExceptionCode {
    TOPIC_NOT_FOUND(404, "존재하지 않는 대결주제 입니다.", "TOPIC_NOT_FOUND"),
    TOPIC_NOT_PUBLIC(403, "비공개 또는 접근이 제한된 대결 주제입니다.", "TOPIC_NOT_PUBLIC"),
    TOPIC_CREATOR_ONLY(403, "작성자만 볼 수 있는 대결주제입니다.", "TOPIC_CREATOR_ONLY"),
    BAD_WORD_DETECTED(422, "입력한 내용 중 비속어가 포함되어 있습니다.", "BAD_WORD_DETECTED"),
    TOPIC_NOT_UNLISTED(400, "해당 대결주제는 비공개 링크로 공유할 수 없습니다.", "TOPIC_NOT_UNLISTED");


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

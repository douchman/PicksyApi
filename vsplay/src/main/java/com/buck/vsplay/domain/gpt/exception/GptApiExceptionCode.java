package com.buck.vsplay.domain.gpt.exception;

import com.buck.vsplay.global.exception.BaseExceptionCode;

public enum GptApiExceptionCode implements BaseExceptionCode {

    GPT_API_ERROR(500, "GPT API 오류","GPT_API_ERROR");

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

    GptApiExceptionCode(int status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}

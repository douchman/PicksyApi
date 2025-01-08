package com.buck.vsplay.global.util.aws.s3.exception;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum S3ExceptionCode implements BaseExceptionCode {
    FILE_EMPTY_OR_INVALID(400, "파일이 비어있거나 유효하지 않습니다.", "S3_001"),
    UPLOAD_FAILED(500, "업로드에 실패했습니다.", "S3_002");

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

    S3ExceptionCode(int status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}

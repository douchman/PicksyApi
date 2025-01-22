package com.buck.vsplay.domain.vstopic.exception.playrecord;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum PlayRecordExceptionCode implements BaseExceptionCode {
    RECORD_NOT_FOUND(404, "존재하지 대결기록 입니다.", "PLAY_RECORD_001"),
    TOURNAMENT_CREATION_FAILED(500, "대진표 생성 중 오류가 발생했습니다.", "PLAY_RECORD_002")
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

    PlayRecordExceptionCode(Integer status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}

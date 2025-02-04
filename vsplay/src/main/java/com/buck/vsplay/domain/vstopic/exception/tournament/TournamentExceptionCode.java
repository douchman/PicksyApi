package com.buck.vsplay.domain.vstopic.exception.tournament;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum TournamentExceptionCode implements BaseExceptionCode {

    SERVER_ERROR(500, "토너먼트 처리 중 서버 오류가 발생했습니다.", "TOURNAMENT_001"),
    TOURNAMENT_INVALID(400, "진행할 수 없는 토너먼트 입니다.", "TOURNAMENT_002"),
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

    TournamentExceptionCode(Integer status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}

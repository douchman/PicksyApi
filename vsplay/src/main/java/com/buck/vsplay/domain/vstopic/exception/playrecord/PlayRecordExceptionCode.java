package com.buck.vsplay.domain.vstopic.exception.playrecord;

import com.buck.vsplay.global.exception.BaseExceptionCode;
import lombok.Getter;

@Getter
public enum PlayRecordExceptionCode implements BaseExceptionCode {
    RECORD_NOT_FOUND(404, "존재하지 대결기록 입니다.", "RECORD_NOT_FOUND"),
    TOURNAMENT_CREATION_FAILED(500, "대진표 생성 중 오류가 발생했습니다.", "TOURNAMENT_CREATION_FAILED"),
    MATCH_NOT_FOUND(404, "존재하지 않는 엔트리 매치 입니다.", "MATCH_NOT_FOUND"),
    MATCH_NOT_ASSOCIATED_WITH_RECORD(400, "대결기록과 매치가 일치하지 않습니다.", "MATCH_NOT_ASSOCIATED_WITH_RECORD"),
    DUPLICATE_WINNER_LOSER_ENTRY(400, "승리 엔트리와 패배엔트리가 동일합니다.", "DUPLICATE_WINNER_LOSER_ENTRY"),
    MATCH_ALREADY_COMPLETED(400, "이미 완료된 매치입니다.", "MATCH_ALREADY_COMPLETED"),
    INVALID_ENTRY_FOR_MATCH(400, "매치에 포함되지 않는 엔트리입니다.", "INVALID_ENTRY_FOR_MATCH"),
    PLAY_RECORD_ALREADY_COMPLETED(400, "이미 완료된 대결기록입니다.", "PLAY_RECORD_ALREADY_COMPLETED"),
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

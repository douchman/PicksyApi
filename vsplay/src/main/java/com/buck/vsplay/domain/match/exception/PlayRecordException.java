package com.buck.vsplay.domain.match.exception;

import com.buck.vsplay.global.exception.BaseException;

public class PlayRecordException extends BaseException {
    public PlayRecordException(PlayRecordExceptionCode playRecordExceptionCode) {
        super(playRecordExceptionCode);
    }
}

package com.buck.vsplay.domain.notice.exception;

import com.buck.vsplay.global.exception.BaseException;

public class NoticeException extends BaseException {
    public NoticeException(NoticeExceptionCode noticeExceptionCode) {
        super(noticeExceptionCode);
    }
}

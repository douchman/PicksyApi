package com.buck.vsplay.domain.vstopic.exception.entry;

import com.buck.vsplay.global.exception.BaseException;

public class EntryException extends BaseException {
    public EntryException(EntryExceptionCode entryExceptionCode) {
        super(entryExceptionCode);
    }
}

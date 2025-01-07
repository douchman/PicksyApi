package com.buck.vsplay.domain.member.exception;

import com.buck.vsplay.global.exception.BaseException;

public class MemberException extends BaseException {
    public MemberException(MemberExceptionCode memberExceptionCode) {
        super(memberExceptionCode);
    }
}

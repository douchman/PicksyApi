package com.buck.vsplay.domain.inquiry.constants;

import lombok.Getter;

@Getter
public enum InquiryType {
    GENERAL("이용 문의"),
    SUGGESTION("기능 제안"),
    ACCOUNT("계정 관련"),
    BUG("오류 제보"),
    ETC("기타");

    private final String description;

    InquiryType(final String description) {
        this.description = description;
    }

}

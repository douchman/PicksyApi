package com.buck.vsplay.global.constants;

import lombok.Getter;

@Getter
public enum ModerationStatus {
    WAITING("WAITING", "검토 대기"),
    PASSED("PASSED", "통과"),
    BLOCKED("BLOCKED", "비속어 검출"),
    ERROR("ERROR", "처리 실패");

    private final String status;
    private final String description;

    ModerationStatus(String status, String description) {
        this.status = status;
        this.description = description;
    }
}

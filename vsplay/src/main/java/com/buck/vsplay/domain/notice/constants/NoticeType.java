package com.buck.vsplay.domain.notice.constants;

import lombok.Getter;

@Getter
public enum NoticeType {
    DEFAULT("일반 공지"),
    UPDATE("업데이트"),
    EVENT("이벤트"),
    ALERT("긴급안내"),
    MAINTENANCE("점검");

    private final String description;

    NoticeType(final String description) {this.description = description;}

}

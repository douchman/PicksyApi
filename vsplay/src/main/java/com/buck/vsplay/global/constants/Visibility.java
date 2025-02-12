package com.buck.vsplay.global.constants;

import lombok.Getter;

@Getter
public enum Visibility {
    PUBLIC("PUBLIC", "전체공개"),
    PRIVATE("PRIVATE", "비공개"),
    FRIEND_ONLY("FRIEND_ONLY", "친구 공개(링크)");

    private final String code;
    private final String description;

    Visibility(String code, String description){
        this.code = code;
        this.description = description;
    }
}

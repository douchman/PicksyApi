package com.buck.vsplay.global.redis.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisKeyPrefix {
    public static final String USER = "user_";
    public static final String TOPIC = "topic_";
    public static final String ENTRY = "entry_";
}

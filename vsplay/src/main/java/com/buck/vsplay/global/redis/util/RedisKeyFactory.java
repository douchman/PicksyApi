package com.buck.vsplay.global.redis.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisKeyFactory {
    public static String user(Long memberId){
        return RedisKeyPrefix.USER + memberId;
    }
    public static String topicEntries(Long topicId) { return RedisKeyPrefix.TOPIC_ENTRIES + topicId; }
}

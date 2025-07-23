package com.buck.vsplay.global.redis.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisKeyFactory {
    public static String user(Long memberId){
        return RedisKeyPrefix.USER + memberId;
    }
}

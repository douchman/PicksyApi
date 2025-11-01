package com.buck.vsplay.domain.entry.service.impl;

import com.buck.vsplay.domain.entry.dto.EntryDto;
import com.buck.vsplay.global.redis.util.RedisKeyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EntryCache implements IEntryCache {

    private static final Duration TTL = Duration.ofMinutes(30); // 만료시간 30m

    private final RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public List<EntryDto.Entry> getCachedEntries(Long topicId) {
        String cacheKey = RedisKeyFactory.topicEntries(topicId);
        return (List<EntryDto.Entry>)redisTemplate.opsForValue().get(cacheKey);
    }

    @Override
    public void putEntries(Long topicId, List<EntryDto.Entry> entries) {
        redisTemplate.opsForValue().set(
                RedisKeyFactory.topicEntries(topicId),
                entries,
                TTL);
    }

    @Override
    public void evictEntries(Long topicId) {
        redisTemplate.delete(RedisKeyFactory.topicEntries(topicId));
    }
}

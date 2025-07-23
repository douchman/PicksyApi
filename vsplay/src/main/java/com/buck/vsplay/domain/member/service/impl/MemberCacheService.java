package com.buck.vsplay.domain.member.service.impl;

import com.buck.vsplay.domain.member.dto.CachedMemberDto;
import com.buck.vsplay.domain.member.entity.Member;
import com.buck.vsplay.domain.member.exception.MemberException;
import com.buck.vsplay.domain.member.exception.MemberExceptionCode;
import com.buck.vsplay.domain.member.repository.MemberRepository;
import com.buck.vsplay.domain.member.service.IMemberCacheService;
import com.buck.vsplay.global.redis.util.RedisKeyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberCacheService implements IMemberCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberRepository memberRepository;

    private static final Duration TTL = Duration.ofMinutes(30); // 만료시간 30m

    @Override
    public CachedMemberDto getMember(Long memberId) {

        String key = RedisKeyFactory.user(memberId);
        Object cached = redisTemplate.opsForValue().get(key);

        if(cached instanceof CachedMemberDto dto) {
            log.info("✅ Redis Cache Hit");
            return dto;
        }

        // 캐시 미존재 : cache miss
        log.info("❌ Redis Cache Missed");
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new MemberException(MemberExceptionCode.MEMBER_NOT_FOUND));

        CachedMemberDto cachedMember = CachedMemberDto.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .memberName(member.getMemberName())
                .build();

        redisTemplate.opsForValue().set(key, cachedMember, TTL);
        return cachedMember;
    }
}

package com.buck.vsplay.domain.member.validator;

import com.buck.vsplay.domain.member.exception.MemberException;
import com.buck.vsplay.domain.member.exception.MemberExceptionCode;
import com.buck.vsplay.domain.member.repository.BannedIdRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberInfoValidator {
    private final ObjectMapper objectMapper;
    private static final String BANNED_WORDS_KEY = "banned-words";
    private final RedisTemplate<String, Object> redisTemplate;
    private final BannedIdRepository bannedIdRepository;

    public void validateLoginId(String loginId) {
        validateAgainstBannedWords(loginId, MemberExceptionCode.BANNED_ID_USED);
    }

    public void validateNickname(String nickname) {
        validateAgainstBannedWords(nickname, MemberExceptionCode.BANNED_NICKNAME_USED);
    }

    private void validateAgainstBannedWords(String targetWord, MemberExceptionCode exceptionCode) {
        Set<String> bannedWords = getBannedWords();
        if(bannedWords.contains(targetWord)) throw new MemberException(exceptionCode);
    }

    private Set<String> getBannedWords() {
        String cached = (String) redisTemplate.opsForValue().get(BANNED_WORDS_KEY);

        if(cached == null) {
            Set<String> bannedWords = new HashSet<>(bannedIdRepository.findAllBannedWords());
            cacheBannedWords(bannedWords);
            return bannedWords;
        }

        try {
            return objectMapper.readValue(cached, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("금칙어 파싱 오류 " , e);
            return Collections.emptySet();
        }
    }

    private void cacheBannedWords(Set<String> bannedWords) {
        try{
            String json = objectMapper.writeValueAsString(bannedWords);
            redisTemplate.opsForValue().set(BANNED_WORDS_KEY, json);
        } catch (JsonProcessingException e){
            log.error("금칙어 캐싱 오류", e);
        }
    }
}

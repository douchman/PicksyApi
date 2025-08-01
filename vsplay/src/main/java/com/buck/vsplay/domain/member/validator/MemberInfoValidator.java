package com.buck.vsplay.domain.member.validator;

import com.buck.vsplay.domain.member.dto.MemberDto;
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
import java.util.Locale;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberInfoValidator {
    private final ObjectMapper objectMapper;
    private static final String BANNED_WORDS_KEY = "banned-words";
    private final RedisTemplate<String, Object> redisTemplate;
    private final BannedIdRepository bannedIdRepository;

    private static final String LOGIN_ID_REGEX = "^[a-z0-9]{4,20}$";
    private static final String NICKNAME_REGEX = "^[a-z0-9가-힣]{1,12}$";
    private static final String PASSWORD_REGEX = "^.{4,20}$";

    public void validateRegisterMember(MemberDto.CreateMemberRequest createMemberRequest) {
        validateLoginId(createMemberRequest.getLoginId());
        validateNickname(createMemberRequest.getMemberName());
        validatePassword(createMemberRequest.getPassword());
    }

    public void validateLoginId(String loginId) {
        validateLoginIdFormats(loginId);
        validateAgainstBannedWords(loginId, MemberExceptionCode.BANNED_ID_USED);
    }

    public void validateNickname(String nickname) {
        validateNickNameFormats(nickname);
        validateAgainstBannedWords(nickname, MemberExceptionCode.BANNED_NICKNAME_USED);
    }

    public void validatePassword(String password) {
        validatePasswordFormats(password);
    }

    private void validateAgainstBannedWords(String targetWord, MemberExceptionCode exceptionCode) {
        Set<String> bannedWords = getBannedWords();
        if(bannedWords.contains(targetWord.toLowerCase(Locale.ROOT))) {
            throw new MemberException(exceptionCode);
        }
    }

    private void cacheBannedWords(Set<String> bannedWords) {
        try{
            String json = objectMapper.writeValueAsString(bannedWords);
            redisTemplate.opsForValue().set(BANNED_WORDS_KEY, json);
        } catch (JsonProcessingException e){
            log.error("금칙어 캐싱 오류 message={}", e.getMessage());
        }
    }

    private void validateLoginIdFormats(String loginId){
        if( loginId == null || loginId.isBlank() || !loginId.matches(LOGIN_ID_REGEX)) {
            throw new MemberException(MemberExceptionCode.INVALID_ID_FORMAT);
        }
    }

    private void validateNickNameFormats(String nickname){
        if( nickname == null || nickname.isBlank() || !nickname.matches(NICKNAME_REGEX)) {
            throw new MemberException(MemberExceptionCode.INVALID_NICKNAME_FORMAT);
        }
    }

    private void validatePasswordFormats(String password){
        if( password == null || password.isBlank() || !password.matches(PASSWORD_REGEX)) {
            throw new MemberException(MemberExceptionCode.INVALID_PASSWORD_FORMAT);
        }
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
            log.error("금칙어 파싱 오류 : key ={}, message={}" , BANNED_WORDS_KEY, e.getMessage());
            return Collections.emptySet();
        }
    }
}

package com.buck.vsplay.global.security.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

import java.util.Map;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublicPaths {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public static final Map<String, Set<HttpMethod>> PUBLIC_ENDPOINTS = Map.ofEntries(
            Map.entry("/member/login", Set.of(HttpMethod.POST)),
            Map.entry("/member", Set.of(HttpMethod.POST)),
            Map.entry("/member/auth", Set.of(HttpMethod.GET)),
            Map.entry("/topics/visibilities", Set.of(HttpMethod.GET)),
            Map.entry("/topics", Set.of(HttpMethod.GET)),
            Map.entry("/topics/*/tournaments", Set.of(HttpMethod.GET)),
            Map.entry("/topics/*", Set.of(HttpMethod.GET)),
            Map.entry("/topics/link/*", Set.of(HttpMethod.GET)),
            Map.entry("/topics/*/play-records", Set.of(HttpMethod.POST)),
            Map.entry("/topics/play-records/*/matches", Set.of(HttpMethod.GET)),
            Map.entry("/topics/play-records/*/matches/*", Set.of(HttpMethod.PATCH)),
            Map.entry("/topics/*/entries", Set.of(HttpMethod.GET)),
            Map.entry("/topics/*/comments", Set.of(HttpMethod.GET, HttpMethod.POST)),
            Map.entry("/statistics/**", Set.of(HttpMethod.GET)),
            Map.entry("/actuator/prometheus", Set.of(HttpMethod.GET)),
            Map.entry("/contact", Set.of(HttpMethod.POST))
    );

    public static final Map<String, Set<HttpMethod>> OPTIONAL_AUTH_ENDPOINTS = Map.ofEntries(
            Map.entry("/topics/*", Set.of(HttpMethod.GET)),
            Map.entry("/topics/*/tournaments", Set.of(HttpMethod.GET)),
            Map.entry("/member/auth", Set.of(HttpMethod.GET)),
            Map.entry("/statistics/**", Set.of(HttpMethod.GET))
    );

    public static boolean isPublicEndPoint(String endpoint, String method) {
        return PUBLIC_ENDPOINTS.entrySet().stream()
                .anyMatch(
                        entry ->
                                PATH_MATCHER.match(entry.getKey(), endpoint) && entry.getValue().contains(HttpMethod.valueOf(method)));
    }

    /*
     * - public 요청은 허용되지만, 내부 비즈니스 로직에서 SecurityContext 참조가 필요한 경로
     * - 즉, 인증이 필수는 아니지만, 인증 정보(JWT)가 있다면 활용할 수 있도록
     *   JwtVerificationFilter를 실행해 SecurityContext를 설정해야 하는 대상
     */
    public static boolean isOptionalAuthEndPoint(String endpoint, String method) {
        return OPTIONAL_AUTH_ENDPOINTS.entrySet().stream()
                .anyMatch(
                        entry ->
                                PATH_MATCHER.match(entry.getKey(), endpoint) && entry.getValue().contains(HttpMethod.valueOf(method)));
    }


}

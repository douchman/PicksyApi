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
            Map.entry("/topics/visibilities", Set.of(HttpMethod.GET)),
            Map.entry("/topics", Set.of(HttpMethod.GET)),
            Map.entry("/topics/*", Set.of(HttpMethod.GET)),
            Map.entry("/topics/link/*", Set.of(HttpMethod.GET)),
            Map.entry("/topics/*/play-records", Set.of(HttpMethod.POST)),
            Map.entry("/topics/play-records/*/matches", Set.of(HttpMethod.GET)),
            Map.entry("/topics/play-records/*/matches/*", Set.of(HttpMethod.PATCH)),
            Map.entry("/topics/*/entries", Set.of(HttpMethod.GET)),
            Map.entry("/topics/*/comments", Set.of(HttpMethod.GET, HttpMethod.POST)),
            Map.entry("/statistics/**", Set.of(HttpMethod.GET))
    );

    public static boolean isPublicEndPoint(String endpoint, String method) {
        return PUBLIC_ENDPOINTS.entrySet().stream()
                .anyMatch(
                        entry ->
                                PATH_MATCHER.match(entry.getKey(), endpoint) && entry.getValue().contains(HttpMethod.valueOf(method)));
    }

}

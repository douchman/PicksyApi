package com.buck.vsplay.global.security.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityPaths {
    private static final String[] PUBLIC_POST_PATHS = {
            "/member",
            "/member/login"
    };

    public static String [] getPublicPostPaths() {
        return PUBLIC_POST_PATHS;
    }
}

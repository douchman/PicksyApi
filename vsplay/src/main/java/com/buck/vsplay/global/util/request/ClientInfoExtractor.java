package com.buck.vsplay.global.util.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientInfoExtractor {

    public static String extractIpFromRequest() {
        HttpServletRequest request = getCurrentRequest();
        if(request == null) return null;

        String ip = request.getHeader("x-forwarded-for");
        if(ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }

        return ip;
    }

    public static String extractUserAgentFromRequest( ) {
        HttpServletRequest request = getCurrentRequest();
        if(request == null) return null;

        return request.getHeader("User-Agent");
    }

    private static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if(attrs == null) {
            log.warn("HttpServletRequest is not available in current context");
        }
        return attrs != null ? attrs.getRequest() : null;
    }
}

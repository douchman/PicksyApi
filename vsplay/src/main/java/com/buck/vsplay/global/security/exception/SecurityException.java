package com.buck.vsplay.global.security.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;

@Setter
@Getter
public class SecurityException extends AuthenticationException {
    private final SecurityExceptionCode securityExceptionCode;

    public SecurityException(SecurityExceptionCode securityExceptionCode) {
        super(securityExceptionCode.getMessage());
        this.securityExceptionCode = securityExceptionCode;
    }

}

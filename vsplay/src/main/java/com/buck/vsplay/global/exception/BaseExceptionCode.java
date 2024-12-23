package com.buck.vsplay.global.exception;

public interface BaseExceptionCode {
    Integer getStatus();
    String getMessage();
    String getErrorCode();
}

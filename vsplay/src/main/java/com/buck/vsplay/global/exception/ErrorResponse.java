package com.buck.vsplay.global.exception;

import com.buck.vsplay.global.util.DateTimeUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Set;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final Integer status;
    private final String message;
    private final String errorCode;
    private final String timestamp;
    private final List<FieldError> fieldErrors;
    private final List<ConstraintViolationError> violationErrors;

    public static ErrorResponse of(BaseExceptionCode exceptionCode) {
        return ErrorResponse.builder()
                .status(exceptionCode.getStatus())
                .message(exceptionCode.getMessage())
                .errorCode(exceptionCode.getErrorCode())
                .timestamp(DateTimeUtil.formatNow())
                .build();
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        return ErrorResponse.builder()
                .fieldErrors(FieldError.of(bindingResult))
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(DateTimeUtil.formatNow())
                .build();
    }

    public static ErrorResponse of(Set<ConstraintViolation<?>> constraintViolations) {
        return ErrorResponse.builder()
                .violationErrors(ConstraintViolationError.of(constraintViolations))
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(DateTimeUtil.formatNow())
                .build();
    }
}

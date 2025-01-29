package com.buck.vsplay.global.exception;


import com.buck.vsplay.global.util.DateTimeUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(
            BaseException baseException) {
        ErrorResponse errorResponse = ErrorResponse.of(baseException.getBaseExceptionCode());

        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    /**
     * 예상하지 못한 RuntimeException 을 처리하여 500 Internal Server Error로 응답
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedRuntimeException(RuntimeException exception) {
        log.error("Unexpected error occurred: ", exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("요청한 내용을 처리하던 중 예상하지 못한 오류가 발생했습니다.")
                .errorCode("INTERNAL_SERVER_ERROR")
                .timestamp(DateTimeUtil.formatNow())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return  ErrorResponse.of(exception.getBindingResult());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {

        return  ErrorResponse.of(exception.getConstraintViolations());
    }
}

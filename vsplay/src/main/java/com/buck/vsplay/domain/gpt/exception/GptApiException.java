package com.buck.vsplay.domain.gpt.exception;

import com.buck.vsplay.global.exception.BaseException;

public class GptApiException extends BaseException {
    public GptApiException(GptApiExceptionCode gptApiExceptionCode) {
        super(gptApiExceptionCode);
    }
}

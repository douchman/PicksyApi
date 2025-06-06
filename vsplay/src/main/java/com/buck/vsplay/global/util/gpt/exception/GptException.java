package com.buck.vsplay.global.util.gpt.exception;

import com.buck.vsplay.global.exception.BaseException;

public class GptException extends BaseException {
    public GptException(GptExceptionCode gptApiExceptionCode) {
        super(gptApiExceptionCode);
    }
}

package com.buck.vsplay.global.exception;


import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    /* SonarLint : RuntimeException 을 상속하기에 직렬화 경고를 받지만, BaseException 의 직렬화는 해당사항 없으므로 직렬화 제외 */
    private final transient BaseExceptionCode baseExceptionCode;

    public BaseException(final BaseExceptionCode baseExceptionCode) {
        super(baseExceptionCode.getMessage());
        this.baseExceptionCode = baseExceptionCode;
    }
}

package com.buck.vsplay.domain.vstopic.exception.vstopic;

import com.buck.vsplay.global.exception.BaseException;

public class VsTopicException extends BaseException {
    public VsTopicException(VsTopicExceptionCode vsTopicExceptionCode) {
        super(vsTopicExceptionCode);
    }
}

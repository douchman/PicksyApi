package com.buck.vsplay.global.util.aws.s3.exception;

import com.buck.vsplay.global.exception.BaseException;

public class S3Exception extends BaseException {
    public S3Exception(S3ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}

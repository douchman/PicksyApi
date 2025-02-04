package com.buck.vsplay.global.util.aws.s3.dto;


import com.buck.vsplay.global.constants.MediaType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class S3Dto {

    @Builder
    @Getter
    public static class S3UploadResult {
        String objectKey;
        MediaType mediaType;
    }
}

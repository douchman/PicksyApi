package com.buck.vsplay.domain.upload.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PresignedUrlDto {

    @Getter
    @Setter
    public static class RequestFile{
        String originalFileName;
        String contentType;
    }

    @Getter
    @Setter
    @Builder
    public static class PresignedFile{
        String objectKey;
        String presignedUploadUrl;
    }

    @Setter
    @Getter
    @Builder
    public static class GeneratePresignedUrlRequest{
        List<RequestFile> requestFiles;
    }

    @Setter
    @Getter
    @Builder
    public static class GeneratePresignedUrlResponse{
        List<PresignedFile> presignedFiles;
    }

}

package com.buck.vsplay.domain.upload.dto;

import com.buck.vsplay.global.constants.MediaType;
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
        String originalFileName;
        String objectKey;
        String presignedUploadUrl;
        MediaType mediaType;
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

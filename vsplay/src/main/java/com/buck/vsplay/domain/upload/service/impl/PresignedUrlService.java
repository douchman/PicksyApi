package com.buck.vsplay.domain.upload.service.impl;

import com.buck.vsplay.domain.member.dto.CachedMemberDto;
import com.buck.vsplay.domain.upload.dto.PresignedUrlDto;
import com.buck.vsplay.domain.upload.service.IPresignedUrlService;
import com.buck.vsplay.global.security.service.impl.AuthUserService;
import com.buck.vsplay.global.util.aws.s3.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class PresignedUrlService implements IPresignedUrlService {

    private final S3Util s3Util;
    private final AuthUserService authUserService;

    @Override
    public PresignedUrlDto.GeneratePresignedUrlResponse generateClientUploadUrl(PresignedUrlDto.GeneratePresignedUrlRequest generatePresignedUrlRequest) {
        CachedMemberDto cachedMemberDto = authUserService.getCachedMember();

        List<PresignedUrlDto.PresignedFile> presignedFiles = generatePresignedUrlRequest.getRequestFiles().stream()
                .map( file -> {
                    String objectKey = generateObjectKey(cachedMemberDto.getId(), file.getOriginalFileName());
                    String presignedUploadUrl = s3Util.generatePreSignedUploadUrl(
                            objectKey,
                            file.getContentType(),
                            Duration.ofMinutes(3));

                    return PresignedUrlDto.PresignedFile.builder()
                            .originalFileName(file.getOriginalFileName())
                            .objectKey(objectKey)
                            .presignedUploadUrl(presignedUploadUrl)
                            .mediaType(s3Util.determineMediaType(file.getContentType()))
                            .build();
                }).toList();

        return PresignedUrlDto.GeneratePresignedUrlResponse.builder()
                .presignedFiles(presignedFiles)
                .build();
    }

    private String generateObjectKey(Long memberId, String originalFilename) {
        return memberId + "/" + s3Util.generateRandomFileName() + s3Util.getFileExtension(originalFilename).toLowerCase(Locale.ROOT);
    }
}

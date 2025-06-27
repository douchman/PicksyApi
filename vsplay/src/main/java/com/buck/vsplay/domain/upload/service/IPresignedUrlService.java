package com.buck.vsplay.domain.upload.service;

import com.buck.vsplay.domain.upload.dto.PresignedUrlDto;

public interface IPresignedUrlService {
    PresignedUrlDto.GeneratePresignedUrlResponse generateClientUploadUrl(PresignedUrlDto.GeneratePresignedUrlRequest generatePresignedUrlRequest);
}

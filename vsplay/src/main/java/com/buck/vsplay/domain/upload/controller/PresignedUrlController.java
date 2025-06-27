package com.buck.vsplay.domain.upload.controller;


import com.buck.vsplay.domain.upload.dto.PresignedUrlDto;
import com.buck.vsplay.domain.upload.service.impl.PresignedUrlService;
import com.buck.vsplay.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("uploads")
@Slf4j
public class PresignedUrlController {

    private final PresignedUrlService preSignedUrlService;

    @PostMapping("presigned-url")
    public ResponseEntity<SingleResponseDto<PresignedUrlDto.GeneratePresignedUrlResponse>> generateClientUploadUrl(
            @RequestBody PresignedUrlDto.GeneratePresignedUrlRequest generatePresignedUrlRequest
    ){
        log.info("Pre Signed URL 요청 확인");
        return new ResponseEntity<>(new SingleResponseDto<>(HttpStatus.OK.value(), preSignedUrlService.generateClientUploadUrl(generatePresignedUrlRequest)), HttpStatus.OK);
    }
}

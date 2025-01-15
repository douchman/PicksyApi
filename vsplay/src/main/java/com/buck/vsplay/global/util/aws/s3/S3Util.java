package com.buck.vsplay.global.util.aws.s3;

import com.buck.vsplay.global.constants.MediaType;
import com.buck.vsplay.global.util.aws.s3.dto.S3Dto;
import com.buck.vsplay.global.util.aws.s3.exception.S3Exception;
import com.buck.vsplay.global.util.aws.s3.exception.S3ExceptionCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
public class S3Util {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    public S3Util(S3Presigner s3Presigner, S3Client s3Client) {
        this.s3Presigner = s3Presigner;
        this.s3Client = s3Client;
    }

    public String getUploadedObjectUrl(String objectKey){
        // S3 GetObject
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        // PreSigned URL
        GetObjectPresignRequest preSignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofSeconds(30))
                .build();

        return s3Presigner.presignGetObject(preSignRequest).url().toString();
    }

    public S3Dto.S3UploadResult putObject(MultipartFile file, String objectPath) {
        try {

            validateFile(file);

            String fileExtension = getFileExtension(file.getOriginalFilename());
            String objectName = generateRandomFileName() + fileExtension;
            String objectKey = objectPath + objectName;

            MediaType mediaType = determineMediaType(file.getContentType());

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return S3Dto.S3UploadResult.builder()
                    .objectKey(objectKey)
                    .mediaType(mediaType)
                    .build();

        } catch ( IOException e){
            throw new S3Exception(S3ExceptionCode.UPLOAD_FAILED);
        }
    }

    private String generateRandomFileName(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String getFileExtension(String originalFileName){
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new S3Exception(S3ExceptionCode.FILE_EMPTY_OR_INVALID);
        }
    }

    public String buildS3Path(String ... parts){
        String joinedPath = String.join("/", parts);
        return joinedPath.endsWith("/") ? joinedPath : joinedPath + "/";
    }

    private MediaType determineMediaType(String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            return MediaType.IMAGE;
        }
        if (contentType.startsWith("image")) {
            return MediaType.IMAGE;
        } else if (contentType.startsWith("video")) {
            return MediaType.VIDEO;
        }
        return MediaType.IMAGE;
    }
}

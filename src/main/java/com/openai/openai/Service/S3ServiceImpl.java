package com.openai.openai.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class S3ServiceImpl implements S3Service{


    @Value("${s3.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;
    @Override
    public String uploadFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF files are supported");
        }
        String key = "documents/%s-/%s".formatted(
                 UUID.randomUUID(), file.getOriginalFilename());
        try {
            PutObjectRequest request = PutObjectRequest.builder().
                    bucket(bucketName).
                    key(key).
                    contentType(file.getContentType()).
                    build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("Uploaded file to storage: {}", key);
            return key;
        } catch (IOException e) {

            throw new RuntimeException("Error uploading file in s3",e);
        }

    }

    @Override
    public InputStream downloadFile(String key) {

        GetObjectRequest request= GetObjectRequest.builder().bucket(bucketName).key(key).build();
        return s3Client.getObject(request);
    }

    @Override
    public void deleteFile(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
        log.info("Deleted file from storage: {}", key);
    }
}

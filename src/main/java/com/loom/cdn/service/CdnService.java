package com.loom.cdn.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.loom.cdn.model.FileMetadata;
import com.loom.cdn.repository.FileMetadataRepository;
import com.loom.cdn.redis.RedisPublisher;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;

@Service
public class CdnService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private RedisPublisher redisPublisher;
    
    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType();
            long size = file.getSize();

            // 1. Save to MinIO
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(originalFilename)
                    .stream(inputStream, size, -1)
                    .contentType(contentType)
                    .build()
            );

            // 2. Save to Database (Using Standard Java Setters)
            String fileUrl = "http://localhost:9000/" + bucketName + "/" + originalFilename;
            
            // --- UPDATED SECTION START ---
            FileMetadata metadata = new FileMetadata(); // Create object manually
            metadata.setOriginalFilename(originalFilename);
            metadata.setStoredFilename(originalFilename);
            metadata.setContentType(contentType);
            metadata.setFileSize(size);
            metadata.setUrl(fileUrl);
            metadata.setUploadTime(LocalDateTime.now());
            // --- UPDATED SECTION END ---
            
            fileMetadataRepository.save(metadata);

            // 3. Broadcast to Redis
            String message = "FILE_UPLOADED:" + originalFilename;
            redisPublisher.publish(message);

            return "File uploaded, stored in DB & broadcasted: " + originalFilename;

        } catch (MinioException | IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return "Error uploading file: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unexpected error: " + e.getMessage();
        }
    }

    public InputStream getFile(String filename) {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error fetching file: " + e.getMessage());
        }
    }

    public java.util.List<FileMetadata> getAllFiles() {
        return fileMetadataRepository.findAll();
    }
}
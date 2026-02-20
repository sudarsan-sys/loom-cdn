package com.loom.cdn.repository;

import com.loom.cdn.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    
    // Custom query: Find files by their original name
    List<FileMetadata> findByOriginalFilename(String originalFilename);
}
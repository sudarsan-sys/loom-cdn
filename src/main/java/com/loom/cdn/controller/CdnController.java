package com.loom.cdn.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Cleaned up imports
import org.springframework.web.multipart.MultipartFile;

import com.loom.cdn.service.CdnService;

@RestController
@RequestMapping("/api/v1/cdn")
public class CdnController {

    @Autowired
    private CdnService cdnService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String response = cdnService.uploadFile(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        // 1. Get stream
        InputStream fileStream = cdnService.getFile(filename);
        
        // 2. Wrap in Resource
        InputStreamResource resource = new InputStreamResource(fileStream);

        // 3. Return with headers
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @GetMapping("/list")
    public java.util.List<com.loom.cdn.model.FileMetadata> listFiles() {
        return cdnService.getAllFiles();
    }
}
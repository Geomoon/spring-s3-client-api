package com.proour.storage.proourstorageapi.controller;

import com.proour.storage.proourstorageapi.entity.Asset;
import com.proour.storage.proourstorageapi.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class S3RestController {

    private final S3Service service;

    @PostMapping("/upload")
    public Map<String, String> upload(@RequestParam MultipartFile file) {
        String fileKey = service.putObject(file);
        Map<String, String> result = new HashMap<>();
        result.put("file_url", service.getObjectUrl(fileKey));
        return result;
    }

    @GetMapping(value = "/get-object", params = "key")
    public ResponseEntity<ByteArrayResource> getObject(@RequestParam String key) {
        Asset asset = service.getObject(key);
        ByteArrayResource resource = new ByteArrayResource(asset.getContent());
        return ResponseEntity
                .ok()
                .header("Content-Type", asset.getContentType())
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @DeleteMapping(value = "/delete-object", params = "key")
    public void deleteObject(@RequestParam String key) {
        service.deleteObject(key);
    }
}

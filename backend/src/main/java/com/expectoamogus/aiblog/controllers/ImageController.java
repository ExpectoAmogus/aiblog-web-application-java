package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.service.impl.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/images")
public class ImageController {
    private final S3Service s3Service;

    public ImageController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/{articleId}/{imageId}")
    public ResponseEntity<String> getImageById(@PathVariable String articleId, @PathVariable Long imageId) throws MalformedURLException {
        List<String> images = s3Service.getImagesByArticleId(articleId);
        if (images.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String imageUrl = images.get(Math.toIntExact(imageId) - 1);
        var image = new UrlResource(imageUrl);
        if (image.exists()) {
            log.info("Image {}", imageUrl);
            return new ResponseEntity<>(imageUrl, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.expectoamogus.aiblog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final S3Service s3Service;


    public ResponseEntity<UrlResource> getImage(String articleId, Long imageId) {
        List<String> images = s3Service.getImagesByArticleId(articleId);
        if (images.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String imageUrl = images.get(Math.toIntExact(imageId) - 1);
        try {
            var image = new UrlResource(imageUrl);
            if (image.exists()) {
                return new ResponseEntity<>(image, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }
}

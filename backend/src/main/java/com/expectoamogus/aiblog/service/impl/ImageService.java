package com.expectoamogus.aiblog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final S3Service s3Service;


    public ResponseEntity<String> getImage(String articleId, Long imageId) {
        List<String> images = s3Service.getImagesByArticleId(articleId);
        if (images.isEmpty() || imageId <= 0 || imageId > images.size()) {
            return ResponseEntity.notFound().build();
        }
        String image = images.get(Math.toIntExact(imageId) - 1);
        if (!image.isEmpty()) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }
}

package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.service.impl.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{articleId}/{imageId}")
    public ResponseEntity<UrlResource> getImageById(@PathVariable String articleId, @PathVariable Long imageId) {
        return imageService.getImage(articleId, imageId);
    }
}

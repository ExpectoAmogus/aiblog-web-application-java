package com.expectoamogus.aiblog.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
    @Mock
    private S3Service s3Service;

    @InjectMocks
    private ImageService imageService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getImage_ValidArticleIdAndImageId_ReturnsImageURL() {
        // Arrange
        String articleId = "article-1";
        Long imageId = 1L;
        List<String> images = new ArrayList<>();
        images.add("url1");
        images.add("url2");
        when(s3Service.getImagesByArticleId(articleId)).thenReturn(images);

        // Act
        ResponseEntity<String> result = imageService.getImage(articleId, imageId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("url1", result.getBody());
    }

    @Test
    void getImage_InvalidArticleId_ReturnsNotFound() {
        // Arrange
        String articleId = "invalid-article";
        Long imageId = 1L;
        when(s3Service.getImagesByArticleId(articleId)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<String> result = imageService.getImage(articleId, imageId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void getImage_ValidArticleIdAndInvalidImageId_ReturnsNotFound() {
        // Arrange
        String articleId = "article-1";
        Long imageId = 3L;
        List<String> images = new ArrayList<>();
        images.add("url1");
        images.add("url2");
        when(s3Service.getImagesByArticleId(articleId)).thenReturn(images);

        // Act
        ResponseEntity<String> result = imageService.getImage(articleId, imageId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }


    @Test
    void getImage_EmptyImageURL_ReturnsOKWithNullBody() {
        // Arrange
        String articleId = "article-1";
        Long imageId = 1L;
        List<String> images = new ArrayList<>();
        images.add("");
        when(s3Service.getImagesByArticleId(articleId)).thenReturn(images);

        // Act
        ResponseEntity<String> result = imageService.getImage(articleId, imageId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
    }
}

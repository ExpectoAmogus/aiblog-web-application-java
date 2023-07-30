package com.expectoamogus.aiblog.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3Service s3Service;

    private final String bucketName = "test-bucket";
    private final String endpointUrl = "https://s3.example.com";

    @BeforeEach
    void setUp() {
        s3Service = new S3Service(s3Client);
        s3Service.setBucketName(bucketName);
        s3Service.setEndpointUrl(endpointUrl);
    }

    @Test
    void uploadFile_ValidFile_UploadsSuccessfully() {
        // Arrange
        String keyName = "test-file.txt";
        byte[] content = "Test content".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "test-file.txt", "text/plain", content);
        PutObjectResponse putObjectResponse = PutObjectResponse.builder().build();

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(putObjectResponse);

        // Act
        s3Service.uploadFile(keyName, file);

        // Assert
        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void uploadFile_IOException_ErrorLogged() throws IOException {
        // Arrange
        String keyName = "test-file.txt";
        byte[] content = "Test content".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "test-file.txt", "text/plain", content);

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenThrow(SdkClientException.builder().message("IO Exception").build());

        // Act and Assert
        assertThrows(SdkClientException.class, () -> s3Service.uploadFile(keyName, file));
    }

    @Test
    void deleteFile_ValidFile_DeletesSuccessfully() {
        // Arrange
        String keyName = "test-file.txt";
        DeleteObjectResponse deleteObjectResponse = DeleteObjectResponse.builder().build();

        when(s3Client.deleteObject(any(DeleteObjectRequest.class))).thenReturn(deleteObjectResponse);

        // Act
        s3Service.deleteFile(keyName);

        // Assert
        verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void getImagesByArticleId_ValidArticleId_ReturnsListOfImageUrls() {
        // Arrange
        String articleId = "article-1";
        String imageKey1 = articleId + "/image1.jpg";
        String imageKey2 = articleId + "/image2.jpg";
        String imageKey3 = articleId + "/image3.jpg";
        ListObjectsV2Response listObjectsResponse1 = ListObjectsV2Response.builder()
                .contents(S3Object.builder().key(imageKey1).build())
                .isTruncated(true)
                .nextContinuationToken("token")
                .build();
        ListObjectsV2Response listObjectsResponse2 = ListObjectsV2Response.builder()
                .contents(S3Object.builder().key(imageKey2).build())
                .isTruncated(true)
                .nextContinuationToken("token2")
                .build();
        ListObjectsV2Response listObjectsResponse3 = ListObjectsV2Response.builder()
                .contents(S3Object.builder().key(imageKey3).build())
                .isTruncated(false)
                .build();

        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class)))
                .thenReturn(listObjectsResponse1)
                .thenReturn(listObjectsResponse2)
                .thenReturn(listObjectsResponse3);

        // Act
        List<String> result = s3Service.getImagesByArticleId(articleId);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(endpointUrl + "/" + imageKey1));
        assertTrue(result.contains(endpointUrl + "/" + imageKey2));
        assertTrue(result.contains(endpointUrl + "/" + imageKey3));
    }

}

package com.expectoamogus.aiblog.dto;

public record ImageDTO(
        Long id,
        String name,
        String originalFileName,
        Long size,
        String contentType,
        boolean isPreviewImage,
        ArticleDTO article
) {
}

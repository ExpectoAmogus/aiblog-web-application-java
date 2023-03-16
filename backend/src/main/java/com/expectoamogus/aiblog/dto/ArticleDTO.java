package com.expectoamogus.aiblog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleDTO(
        Long id,
        String title,
        String content,
        List<ImageDTO> images,
        Long previewImage,
        UserDTO user,
        LocalDateTime dateOfCreated
) {
}

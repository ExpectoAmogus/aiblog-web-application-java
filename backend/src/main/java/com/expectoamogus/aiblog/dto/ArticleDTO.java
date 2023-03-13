package com.expectoamogus.aiblog.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleDTO(
        Long id,
        String title,
        String content,
        List<ImageDTO> images,
        Long previewImage,
        UserDTO user,
        LocalDateTime dateOfCreation
) {
}

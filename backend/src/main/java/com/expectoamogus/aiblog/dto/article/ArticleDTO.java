package com.expectoamogus.aiblog.dto.article;

import com.expectoamogus.aiblog.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleDTO(
        Long id,
        String uuid,
        String title,
        String content,
        List<String> images,
        UserDTO user,
        LocalDateTime dateOfCreated
) {
}

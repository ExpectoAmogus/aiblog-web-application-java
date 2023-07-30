package com.expectoamogus.aiblog.dto.article;

import com.expectoamogus.aiblog.dto.user.UserDTO;

import java.time.LocalDateTime;
import java.util.List;
public record ArticleDTO(
        Long id,
        String uuid,
        String title,
        String content,
        String category,
        Long views,
        List<String> images,
        UserDTO user,
        LocalDateTime dateOfCreated,
        Double trendingScore
) {
    public ArticleDTO withTrendingScore(Double trendingScore) {
        return new ArticleDTO(
                id,
                uuid,
                title,
                content,
                category,
                views,
                images,
                user,
                dateOfCreated,
                trendingScore
        );
    }
}

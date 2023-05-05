package com.expectoamogus.aiblog.dto.mappers;

import com.expectoamogus.aiblog.dto.article.ArticleDTO;
import com.expectoamogus.aiblog.models.Article;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ArticleDTOMapper implements Function<Article, ArticleDTO> {
    @Override
    public ArticleDTO apply(Article article) {
        return new ArticleDTO(
                article.getId(),
                article.getUuid(),
                article.getTitle(),
                article.getContent(),
                article.getCategory(),
                article.getViews(),
                article.getImages(),
                article.getUserDTO(),
                article.getDateOfCreated(),
                0.0
        );
    }
}

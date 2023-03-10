package com.expectoamogus.aiblog.service;

import com.expectoamogus.aiblog.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleFormRepository extends JpaRepository<Article, Long> {
}

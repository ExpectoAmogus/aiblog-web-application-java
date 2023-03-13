package com.expectoamogus.aiblog.service;

import com.expectoamogus.aiblog.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleFormRepository extends JpaRepository<Article, Long> {
    Optional<Article> findById(Long id);
}

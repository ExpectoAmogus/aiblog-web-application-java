package com.expectoamogus.aiblog.service;

import com.expectoamogus.aiblog.models.Article;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleFormRepository extends JpaRepository<Article, Long> {
    @NotNull
    Optional<Article> findById(@NotNull Long id);
}

package com.expectoamogus.aiblog.service;

import com.expectoamogus.aiblog.models.Article;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleFormRepository extends JpaRepository<Article, Long> {
    @NotNull
    Optional<Article> findById(@NotNull Long id);

    @Override
    Page<Article> findAll(Pageable pageable);

    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByTitleContainingOrderByDateOfCreatedDesc(String title, Pageable pageable);
    Page<Article> findByCategoryOrderByDateOfCreatedDesc(String category, Pageable pageable);
    Page<Article> findByTitleContainingAndCategoryOrderByDateOfCreatedDesc(String title, String category, Pageable pageable);
    Page<Article> findByTitleContainingOrderByViewsDesc(String title, Pageable pageable);
}

package com.expectoamogus.aiblog.service;

import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticle(Article article);
}

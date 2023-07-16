package com.expectoamogus.aiblog.service;

import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.models.Comment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentTest;
    @Autowired
    private ArticleFormRepository articleTest;

    @AfterEach
    void tearDown() {
        commentTest.deleteAll();
        articleTest.deleteAll();
    }

    @Test
    void findByArticle() {
        Article article = new Article();
        article.setTitle("test");
        articleTest.save(article);

        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setText("cock");
        commentTest.save(comment);

        assertThat(commentTest.findByArticle(article)).isEqualTo(List.of(comment));
    }
}
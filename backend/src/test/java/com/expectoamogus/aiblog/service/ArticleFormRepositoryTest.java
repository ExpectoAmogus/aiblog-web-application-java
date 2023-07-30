package com.expectoamogus.aiblog.service;

import com.expectoamogus.aiblog.models.Article;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class ArticleFormRepositoryTest {
    @Autowired
    private ArticleFormRepository articleTest;

    @AfterEach
    void tearDown() {
        articleTest.deleteAll();
    }

    @Test
    void articleExistById() {
        Article article = new Article();
        article.setTitle("test");
        articleTest.save(article);

        Long id = article.getId();

        assertThat(articleTest.findById(id)).isNotEmpty();
    }
}

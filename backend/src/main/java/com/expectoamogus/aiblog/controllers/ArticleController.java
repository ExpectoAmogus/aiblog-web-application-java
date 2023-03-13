package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.dto.ArticleDTO;
import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.service.impl.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/all")
    public ResponseEntity<List<ArticleDTO>> articles(){
        List<ArticleDTO> articles = articleService.findAll();
        if (articles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("find/{id}")
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable("id") Long id) {
        ArticleDTO article = articleService.findById(id);
        if (article == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Article> createArticle(Principal principal, @RequestBody Article article) {
        Article newArticle = articleService.saveArticle(principal, article);
        return new ResponseEntity<>(newArticle, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Article> updateArticle(Principal principal, @RequestBody Article article) {
        Article newArticle = articleService.saveArticle(principal, article);
        return new ResponseEntity<>(newArticle, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable("id") Long id) {
        articleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
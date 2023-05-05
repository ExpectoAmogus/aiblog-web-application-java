package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.dto.article.ArticleDTO;
import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.service.impl.ArticleService;
import com.expectoamogus.aiblog.service.impl.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final S3Service s3Service;

    @GetMapping("/all")
    public ResponseEntity<List<ArticleDTO>> articles(){
        List<ArticleDTO> articles = articleService.findAllOrderByDateDesc();
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }
    @GetMapping("/popular")
    public ResponseEntity<List<ArticleDTO>> popularArticles(){
        List<ArticleDTO> articles = articleService.findAllOrderByViewsDesc();
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }
    @GetMapping("/trending")
    public ResponseEntity<List<ArticleDTO>> trendingArticles(){
        List<ArticleDTO> articles = articleService.findTrending();
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable("id") Long id) {
        ArticleDTO article = articleService.findById(id);
        if (article == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('devs:write')")
    @PostMapping("/add")
    public ResponseEntity<ArticleDTO> createArticle(
            Principal principal,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") String category,
            @RequestParam("images") List<MultipartFile> images) {
        Article newArticle = articleService.saveArticle(principal, title, content, category, images);
        ArticleDTO articleDTO = articleService.findById(newArticle.getId());
        return new ResponseEntity<>(articleDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('devs:write')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(
            Principal principal,
            @PathVariable("id") Long id,
            @RequestParam(value = "title" ,required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        Article newArticle = articleService.update(principal, id, title, content, images);
        ArticleDTO articleDTO = articleService.findById(newArticle.getId());
        return new ResponseEntity<>(articleDTO, HttpStatus.OK);
    }



    @PreAuthorize("hasAuthority('devs:write')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable("id") Long id) {
        Article article = articleService.findArticleById(id);
        List<String> images = article.getImages();
        articleService.deleteById(id);
        if (images != null && !images.isEmpty()) {
            images.forEach(s3Service::deleteFile);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/views")
    public void incrementArticleViews(@PathVariable Long id) {
        articleService.incrementArticleViews(id);
    }

}

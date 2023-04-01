package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.dto.ArticleDTO;
import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.service.impl.ArticleService;
import com.expectoamogus.aiblog.service.impl.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "images", required = false) List<MultipartFile> files) throws IOException {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setUuid(String.valueOf(UUID.randomUUID()));
        if (files != null) {
            List<String> images = new ArrayList<>();
            for (MultipartFile file : files) {
                String keyName = article.getUuid() + "/" + UUID.randomUUID() + "." + file.getOriginalFilename();
                s3Service.uploadFile(keyName, file);
                images.add(keyName);
            }
            article.setImages(images);
        }
        Article newArticle = articleService.saveArticle(principal, article);
        ArticleDTO articleDTO = articleService.findById(newArticle.getId());
        return new ResponseEntity<>(articleDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('devs:write')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(
            Principal principal,
            @PathVariable("id") Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {
        Article articleToUpdate = articleService.findArticleById(id);
        articleToUpdate.setTitle(title);
        articleToUpdate.setContent(content);
        if (images != null) {
            List<String> newImages = new ArrayList<>();
            for (MultipartFile image : images) {
                String keyName = articleToUpdate.getUuid() + "/" + UUID.randomUUID() + "." + FilenameUtils.getExtension(image.getOriginalFilename());
                s3Service.uploadFile(keyName, image);
                newImages.add(keyName);
            }
            articleToUpdate.setImages(newImages);
        }
        Article newArticle = articleService.saveArticle(principal, articleToUpdate);
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

}

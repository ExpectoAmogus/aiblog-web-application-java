package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.dto.article.ArticleDTO;
import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.service.impl.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/all")
    public ResponseEntity<Page<ArticleDTO>> articles(@RequestParam Optional<String> title,
                                                     @RequestParam Optional<String> category,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "24") int size) {
        var pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(articleService.findAllOrderByDateDesc(title.orElse(""), category.orElse(""), pageable), HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<Page<ArticleDTO>> popularArticles(@RequestParam Optional<String> title,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "6") int size) {
        var pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(articleService.findAllOrderByViewsDesc(title.orElse(""), pageable), HttpStatus.OK);
    }

    @GetMapping("/trending")
    public ResponseEntity<Page<ArticleDTO>> trendingArticles(@RequestParam Optional<String> title,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "6") int size) {
        var pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(articleService.findTrending(title.orElse(""), pageable), HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable("id") Long id) {
        return new ResponseEntity<>(articleService.findById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('devs:write')")
    @PostMapping("/add")
    public ResponseEntity<ArticleDTO> createArticle(
            Principal principal,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") String category,
            @RequestParam("images") List<MultipartFile> images) {
        return new ResponseEntity<>(
                articleService.saveArticle(principal, title, content, category, images),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasAuthority('devs:write')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(
            Principal principal,
            @PathVariable("id") Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        return new ResponseEntity<>(
                articleService.update(principal, id, title, content, images),
                HttpStatus.OK
        );
    }


    @PreAuthorize("hasAuthority('devs:write')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable("id") Long id) {
        articleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/views")
    public void incrementArticleViews(@PathVariable Long id) {
        articleService.incrementArticleViews(id);
    }

}

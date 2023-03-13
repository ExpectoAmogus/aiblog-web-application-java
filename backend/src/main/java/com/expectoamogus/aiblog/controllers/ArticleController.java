package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.service.impl.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public String articles(Model model){
        List<Article> articles = articleService.findAll();
        model.addAttribute("articles", articles);
        return "articles";
    }

    @GetMapping("article/{id}")
    public String articleInfo(@PathVariable Long id, Model model){
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        model.addAttribute("images", article.getImages());
        return "article-info";
    }

}

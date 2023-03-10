package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.service.impl.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public String articles(Model model){
        model.addAttribute("articles", articleService.findAll());
        return "articles";
    }

    @GetMapping("/article/{id}")
    public String articleInfo(@PathVariable Long id, Model model){
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        model.addAttribute("images", article.getImages());
        return "article-info";
    }
}

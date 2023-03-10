package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.service.impl.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('devs:read')")
public class AdminController {
    private final ArticleService articleService;

    @Autowired
    public AdminController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping()
    public String getAdminPage(Model model, Principal principal) {
        UserDetails securityAdmin = ((UserDetails) ((Authentication) principal).getPrincipal());
        if (securityAdmin.getAuthorities().contains(new SimpleGrantedAuthority("devs:write"))) {
            List<Article> article = articleService.findAll();
            model.addAttribute("article", article);
            return "admin";
        } else if (securityAdmin.getAuthorities().contains(new SimpleGrantedAuthority("mods:write"))) {
            List<Article> article = articleService.findAll();
            model.addAttribute("article", article);
            return "moderator";
        }
        return "redirect:/";
    }

    @GetMapping("/article-form")
    public String createArticleForm(Model model){
        model.addAttribute("article", new Article());
        return "article-form";
    }

    @PostMapping("/article-form")
    public String createForm(Principal principal, Article article) {
        articleService.saveArticle(principal, article);
        return "redirect:/admin";
    }

    @GetMapping("/article/delete/{id}")
    public String deleteArticleForm(@PathVariable("id") Long id) {
        articleService.deleteById(id);
        return "redirect:/admin";
    }
}

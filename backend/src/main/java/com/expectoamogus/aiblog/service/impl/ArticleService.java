package com.expectoamogus.aiblog.service.impl;

import com.expectoamogus.aiblog.dto.ArticleDTO;
import com.expectoamogus.aiblog.dto.mappers.ArticleDTOMapper;
import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.models.User;
import com.expectoamogus.aiblog.service.ArticleFormRepository;
import com.expectoamogus.aiblog.service.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ArticleService {
    private final ArticleFormRepository articleFormRepository;
    private final UserRepository userRepository;
    private final ArticleDTOMapper articleDTOMapper;


    @Autowired
    public ArticleService(ArticleFormRepository articleFormRepository, UserRepository userRepository, ArticleDTOMapper articleDTOMapper) {
        this.articleFormRepository = articleFormRepository;
        this.userRepository = userRepository;
        this.articleDTOMapper = articleDTOMapper;
    }

    public ArticleDTO findById(Long id) {
        return articleFormRepository.findById(id)
                .map(articleDTOMapper)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "article with id [%s] not found".formatted(id)
                ));
    }
    public Article findArticleById(Long id) {
        return articleFormRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "article with id [%s] not found".formatted(id)
                ));
    }

    public List<ArticleDTO> findAllOrderByDateDesc() {
        return articleFormRepository.findByOrderByDateOfCreatedDesc()
                .stream()
                .map(articleDTOMapper)
                .collect(Collectors.toList());
    }

    public Article saveArticle(Principal principal, Article article) {
        article.setUser(getUserByPrincipal(principal));
        log.info("Saving new Article. Title: {}; Author: {}", article.getTitle(), article.getUser().getEmail());
        return articleFormRepository.save(article);
    }

    public User getUserByPrincipal(Principal principal) {
        return userRepository.findByEmail(principal.getName()).orElseThrow(() ->
                new UsernameNotFoundException("User does not exists"));
    }

    public void deleteById(Long id) {
        articleFormRepository.deleteById(id);
    }
}

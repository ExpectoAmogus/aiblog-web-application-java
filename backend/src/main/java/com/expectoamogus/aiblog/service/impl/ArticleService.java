package com.expectoamogus.aiblog.service.impl;

import com.expectoamogus.aiblog.dto.article.ArticleDTO;
import com.expectoamogus.aiblog.dto.mappers.ArticleDTOMapper;
import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.models.User;
import com.expectoamogus.aiblog.service.ArticleFormRepository;
import com.expectoamogus.aiblog.service.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleFormRepository articleFormRepository;
    private final UserRepository userRepository;
    private final ArticleDTOMapper articleDTOMapper;
    private final S3Service s3Service;

    public ArticleDTO findById(Long id) {
        return articleFormRepository.findById(id)
                .map(articleDTOMapper)
                .orElseThrow(() -> new EntityNotFoundException(
                        "article with id [%s] not found".formatted(id)
                ));
    }
    public Article findArticleById(Long id) {
        return articleFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "article with id [%s] not found".formatted(id)
                ));
    }

    public List<ArticleDTO> findAllOrderByDateDesc(PageRequest pageable) {
        return articleFormRepository.findAll(pageable)
                .stream()
                .map(articleDTOMapper)
                .sorted(Comparator.comparing(ArticleDTO::dateOfCreated).reversed())
                .collect(Collectors.toList());
    }
    public List<ArticleDTO> findTrending(PageRequest pageable) {
        List<ArticleDTO> articleDTOs = articleFormRepository.findAll(pageable)
                .stream()
                .map(articleDTOMapper)
                .sorted(Comparator.comparing(ArticleDTO::views).reversed())
                .toList();

        double timeConstant = 2592000000.0;
        double exponentBase = Math.E;
        double now = System.currentTimeMillis();
        List<ArticleDTO> articleDTOsWithTrendingScore = new ArrayList<>();
        for (ArticleDTO articleDTO : articleDTOs) {
            double elapsedTime = now - articleDTO.dateOfCreated().toEpochSecond(ZoneOffset.UTC) * 1000;
            double trendingScore = (articleDTO.views() - 1) / Math.pow(exponentBase, elapsedTime / timeConstant);
            ArticleDTO articleDTOWithTrendingScore = articleDTO.withTrendingScore(trendingScore);
            articleDTOsWithTrendingScore.add(articleDTOWithTrendingScore);
        }

        return articleDTOsWithTrendingScore.stream()
                .sorted(Comparator.comparing(ArticleDTO::trendingScore).reversed())
                .toList();
    }
    public List<ArticleDTO> findAllOrderByViewsDesc(PageRequest pageable) {
        return articleFormRepository.findAll(pageable)
                .stream()
                .map(articleDTOMapper)
                .sorted(Comparator.comparing(ArticleDTO::views).reversed())
                .collect(Collectors.toList());
    }

    public ArticleDTO saveArticle(Principal principal, String title, String content, String category, List<MultipartFile> files) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setCategory(category);
        article.setViews(0L);
        article.setUuid(String.valueOf(UUID.randomUUID()));
        getImages(principal, files, article);
        log.info("Saving new Article. Title: {}; Author: {}", article.getTitle(), article.getUser().getEmail());
        Article newArticle = articleFormRepository.save(article);
        return findById(newArticle.getId());
    }

    public User getUserByPrincipal(Principal principal) {
        return userRepository.findByEmail(principal.getName()).orElseThrow(() ->
                new UsernameNotFoundException("User does not exists"));
    }

    public ArticleDTO update(Principal principal, Long id, String title, String content, List<MultipartFile> files) {
        Article articleToUpdate = findArticleById(id);
        articleToUpdate.setTitle(title);
        articleToUpdate.setContent(content);
        getImages(principal, files, articleToUpdate);
        log.info("Update Article. Title: {}; Author: {}", articleToUpdate.getTitle(), articleToUpdate.getUser().getEmail());
        Article newArticle = articleFormRepository.save(articleToUpdate);
        return findById(newArticle.getId());
    }

    private void getImages(Principal principal, List<MultipartFile> files, Article articleToUpdate) {
        if (files != null) {
            List<String> newImages = new ArrayList<>();
            for (MultipartFile image : files) {
                String keyName = articleToUpdate.getUuid() + "/" + UUID.randomUUID() + "." + image.getOriginalFilename();
                s3Service.uploadFile(keyName, image);
                newImages.add(keyName);
            }
            articleToUpdate.setImages(newImages);
        }
        articleToUpdate.setUser(getUserByPrincipal(principal));
    }

    public void deleteById(Long id) {
        Article article = findArticleById(id);
        List<String> images = article.getImages();
        articleFormRepository.deleteById(id);
        if (images != null && !images.isEmpty()) {
            images.forEach(s3Service::deleteFile);
        }
    }

    public void incrementArticleViews(Long id) {
        Article article = articleFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
        article.setViews(article.getViews() + 1);
        articleFormRepository.save(article);
    }

}

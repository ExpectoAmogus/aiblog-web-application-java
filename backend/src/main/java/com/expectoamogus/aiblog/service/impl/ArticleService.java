package com.expectoamogus.aiblog.service.impl;

import com.expectoamogus.aiblog.dto.ArticleDTO;
import com.expectoamogus.aiblog.dto.mappers.ArticleDTOMapper;
import com.expectoamogus.aiblog.utils.MultipartToImageConverter;
import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.models.Image;
import com.expectoamogus.aiblog.models.User;
import com.expectoamogus.aiblog.service.ArticleFormRepository;
import com.expectoamogus.aiblog.service.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ArticleService {
    private final ArticleFormRepository articleFormRepository;
    private final UserRepository userRepository;
    private final MultipartToImageConverter multipartToImageConverter;
    private final ArticleDTOMapper articleDTOMapper;


    @Autowired
    public ArticleService(ArticleFormRepository articleFormRepository, UserRepository userRepository, MultipartToImageConverter multipartToImageConverter, ArticleDTOMapper articleDTOMapper) {
        this.articleFormRepository = articleFormRepository;
        this.userRepository = userRepository;
        this.multipartToImageConverter = multipartToImageConverter;
        this.articleDTOMapper = articleDTOMapper;
    }

    public ArticleDTO findById(Long id) {
        return articleFormRepository.findById(id)
                .map(articleDTOMapper)
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
        List<Image> imageList = new ArrayList<>();
        for (Image image : article.getImages()) {
            if (image.getSize() != 0) {
                image.setPreviewImage(imageList.isEmpty());
                imageList.add(image);
            }
        }
        article.addImagesToArticle(imageList);
        log.info("Saving new Article. Title: {}; Author: {}", article.getTitle(), article.getUser().getEmail());
        Article articleFromDb = articleFormRepository.save(article);
        articleFromDb.setPreviewImageId(articleFromDb.getImages().get(0).getId());
        return articleFormRepository.save(articleFromDb);
    }

    public User getUserByPrincipal(Principal principal) {
        return userRepository.findByEmail(principal.getName()).orElseThrow(() ->
                new UsernameNotFoundException("User does not exists"));
    }

    public void deleteById(Long id) {
        articleFormRepository.deleteById(id);
    }
}

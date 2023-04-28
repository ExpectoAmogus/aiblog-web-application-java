package com.expectoamogus.aiblog.dto;

import com.expectoamogus.aiblog.dto.article.ArticleDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record UserDTO(
        Long id,
        String firstName,
        String email,
        Set<SimpleGrantedAuthority> role,
        String username,
        List<ArticleDTO> articles,
        LocalDateTime dateOfCreated
) {
}

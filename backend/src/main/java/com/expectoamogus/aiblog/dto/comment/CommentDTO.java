package com.expectoamogus.aiblog.dto.comment;

import com.expectoamogus.aiblog.dto.article.ArticleDTO;
import com.expectoamogus.aiblog.dto.user.UserDTO;
import com.expectoamogus.aiblog.models.Comment;

import java.time.LocalDateTime;

public record CommentDTO(
        Long id,
        String username,
        String email,
        String text,
        ArticleDTO article,
        Long userId,
        Long parentId,
        LocalDateTime dateOfCreated
) {
    public static CommentDTO fromEntity(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getUsername(),
                comment.getEmail(),
                comment.getText(),
                comment.getArticleDTO(),
                comment.getUserId(),
                comment.getParentId(),
                comment.getDateOfCreated()
        );
    }
}

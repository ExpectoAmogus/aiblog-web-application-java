package com.expectoamogus.aiblog.dto.comment;

import com.expectoamogus.aiblog.models.Comment;

public record CommentDTO(
        Long id,
        String username,
        String email,
        String text,
        Long articleId,
        Long userId,
        Long parentId,
        String dateOfCreated
) {
    public static CommentDTO fromEntity(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getUsername(),
                comment.getEmail(),
                comment.getText(),
                comment.getArticle().getId(),
                comment.getUserId(),
                comment.getParentId(),
                String.valueOf(comment.getDateOfCreated())
        );
    }
}

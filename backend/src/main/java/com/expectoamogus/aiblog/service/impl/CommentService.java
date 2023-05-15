package com.expectoamogus.aiblog.service.impl;

import com.expectoamogus.aiblog.dto.comment.CommentDTO;
import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.models.Comment;
import com.expectoamogus.aiblog.models.User;
import com.expectoamogus.aiblog.service.ArticleFormRepository;
import com.expectoamogus.aiblog.service.CommentRepository;
import com.expectoamogus.aiblog.service.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleFormRepository articleFormRepository;
    private final UserRepository userRepository;

    public User getUserByPrincipal(Principal principal) {
        return userRepository.findByEmail(principal.getName()).orElseThrow(() ->
                new UsernameNotFoundException("User does not exists"));
    }

    public CommentDTO addCommentToArticle(Long articleId, CommentDTO commentDTO, Principal principal) {
        // First, check if the article exists
        Article article = articleFormRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article with id " + articleId + " not found"));
        User user = getUserByPrincipal(principal);
        // Convert CommentDTO to Comment entity
        Comment comment = new Comment();
        comment.setUsername(user.getUsername());
        comment.setEmail(user.getEmail());
        comment.setText(commentDTO.text());
        comment.setArticle(article);
        if (commentDTO.parentId() != null) {
            comment.setParentId(commentDTO.parentId());
        }
        comment.setUserId(user.getId());

        // Save the comment and return its DTO
        return CommentDTO.fromEntity(commentRepository.save(comment));
    }

    public List<CommentDTO> getCommentsForArticle(Long articleId) {
        // First, check if the article exists
        Article article = articleFormRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article with id " + articleId + " not found"));

        // Get all comments for the article and convert them to DTOs
        List<Comment> comments = commentRepository.findByArticle(article);
        return comments.stream().map(CommentDTO::fromEntity).collect(Collectors.toList());
    }

    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO, Principal principal) {
        // First, check if the comment exists
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + commentId + " not found"));
        User user = getUserByPrincipal(principal);
        // Update the comment and save it
        comment.setUsername(user.getUsername());
        comment.setEmail(user.getEmail());
        comment.setText(commentDTO.text());
        return CommentDTO.fromEntity(commentRepository.save(comment));
    }

    public void deleteComment(Long commentId) {
        // First, check if the comment exists
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + commentId + " not found"));

        // Delete the comment
        commentRepository.delete(comment);
    }

    public CommentDTO addReplyToComment(Long commentId, CommentDTO replyDTO) {
        // First, check if the comment exists
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + commentId + " not found"));

        // Convert ReplyDTO to Reply entity
        Comment reply = new Comment();
        reply.setUsername(replyDTO.username());
        reply.setEmail(replyDTO.email());
        reply.setText(replyDTO.text());
        reply.setArticle(comment.getArticle());
        reply.setParentId(comment.getId());
        reply.setUserId(replyDTO.userId());

        // Save the reply and return its DTO
        return CommentDTO.fromEntity(commentRepository.save(reply));
    }
}

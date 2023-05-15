package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.dto.comment.CommentDTO;
import com.expectoamogus.aiblog.service.impl.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{articleId}")
    public List<CommentDTO> getComments(@PathVariable Long articleId) {
        return commentService.getCommentsForArticle(articleId);
    }

    @PostMapping("/{articleId}")
    public CommentDTO addComment(@PathVariable Long articleId, @RequestBody CommentDTO comment, Principal principal) {
        return commentService.addCommentToArticle(articleId, comment, principal);
    }

    @PatchMapping("/{commentId}")
    public CommentDTO updateComment(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO, Principal principal) {
        return commentService.updateComment(commentId, commentDTO, principal);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PostMapping("/replies/{commentId}")
    public CommentDTO addReply(@PathVariable Long commentId, @RequestBody CommentDTO reply) {
        return commentService.addReplyToComment(commentId, reply);
    }
}

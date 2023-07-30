package com.expectoamogus.aiblog.service.impl;

import com.expectoamogus.aiblog.dto.comment.CommentDTO;
import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.models.Comment;
import com.expectoamogus.aiblog.models.User;
import com.expectoamogus.aiblog.service.ArticleFormRepository;
import com.expectoamogus.aiblog.service.CommentRepository;
import com.expectoamogus.aiblog.service.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleFormRepository articleFormRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

//    @Test
//    void addCommentToArticle_ValidArticle_ReturnsCommentDTO() {
//        // Arrange
//        Long articleId = 1L;
//        String text = "This is a comment.";
//        CommentDTO commentDTO = new CommentDTO(null, "john@example.com", "john@example.com", text, null, 1L, null, LocalDateTime.now());
//        Article article = new Article();
//        Principal principal = mock(Principal.class);
//        User user = new User();
//        user.setId(1L);
//        user.setFirstName("John Doe");
//        user.setEmail("john@example.com");
//
//        when(articleFormRepository.findById(articleId)).thenReturn(Optional.of(article));
//        when(userRepository.findByEmail(principal.getName())).thenReturn(Optional.of(user));
//        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        CommentDTO result = commentService.addCommentToArticle(articleId, commentDTO, principal);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(text, result.text());
//        assertEquals("john@example.com", result.email());
//        assertNull(result.parentId());
//        assertEquals(1L, result.userId());
//    }

    @Test
    void addCommentToArticle_InvalidArticle_ThrowsEntityNotFoundException() {
        // Arrange
        Long articleId = 1L;
        CommentDTO commentDTO = new CommentDTO(null, "john@example.com", "john@example.com", "This is a comment.", null, 1L, null, LocalDateTime.now());
        Principal principal = mock(Principal.class);

        when(articleFormRepository.findById(articleId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> commentService.addCommentToArticle(articleId, commentDTO, principal));
    }

//    @Test
//    void getCommentsForArticle_ValidArticle_ReturnsListOfCommentDTOs() {
//        // Arrange
//        Long articleId = 1L;
//        Article article = new Article();
//        List<Comment> comments = Collections.singletonList(new Comment());
//        when(articleFormRepository.findById(articleId)).thenReturn(Optional.of(article));
//        when(commentRepository.findByArticle(article)).thenReturn(comments);
//
//        // Act
//        List<CommentDTO> result = commentService.getCommentsForArticle(articleId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(comments.size(), result.size());
//    }

    @Test
    void getCommentsForArticle_InvalidArticle_ThrowsEntityNotFoundException() {
        // Arrange
        Long articleId = 1L;

        when(articleFormRepository.findById(articleId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> commentService.getCommentsForArticle(articleId));
    }

//    @Test
//    void updateComment_ValidComment_ReturnsUpdatedCommentDTO() {
//        // Arrange
//        Long commentId = 1L;
//        String updatedText = "Updated comment.";
//        CommentDTO commentDTO = new CommentDTO(null, "john@example.com", "john@example.com", updatedText, null, 1L, null, LocalDateTime.now());
//        Comment comment = new Comment();
//        comment.setId(commentId);
//        Principal principal = mock(Principal.class);
//        User user = new User();
//        user.setId(1L);
//        user.setEmail("john@example.com");
//
//        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
//        when(userRepository.findByEmail(principal.getName())).thenReturn(Optional.of(user));
//        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        CommentDTO result = commentService.updateComment(commentId, commentDTO, principal);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(updatedText, result.text());
//        assertEquals("john@example.com", result.email());
//        assertNull(result.parentId());
//        assertEquals(1L, result.userId());
//    }

    @Test
    void updateComment_InvalidComment_ThrowsEntityNotFoundException() {
        // Arrange
        Long commentId = 1L;
        CommentDTO commentDTO = new CommentDTO(null, "john@example.com", "john@example.com", "Updated comment.", null, 1L, null, LocalDateTime.now());
        Principal principal = mock(Principal.class);

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> commentService.updateComment(commentId, commentDTO, principal));
    }

    @Test
    void deleteComment_ValidComment_DeletesComment() {
        // Arrange
        Long commentId = 1L;
        Comment comment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        commentService.deleteComment(commentId);

        // Assert
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void deleteComment_InvalidComment_ThrowsEntityNotFoundException() {
        // Arrange
        Long commentId = 1L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> commentService.deleteComment(commentId));
    }

//    @Test
//    void addReplyToComment_ValidComment_ReturnsReplyDTO() {
//        // Arrange
//        Long commentId = 1L;
//        String replyText = "This is a reply.";
//        CommentDTO replyDTO = new CommentDTO(null, "jane@example.com", "jane@example.com", replyText, null, 2L, 1L, LocalDateTime.now());
//        Comment comment = new Comment();
//        comment.setId(commentId);
//        Principal principal = mock(Principal.class);
//
//        // Mock an Article object
//        Article article = new Article();
//        article.setId(1L);
//
//        // Set the mocked Article object in the Comment entity
//        comment.setArticle(article);
//
//        // Mock a User object
//        User user = new User();
//        user.setId(2L);
//
//        // Set the mocked User object in the Comment entity
//        comment.setUserId(user.getId());
//
//        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
//        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        CommentDTO result = commentService.addReplyToComment(commentId, replyDTO);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(replyText, result.text());
//        assertEquals("jane@example.com", result.email());
//        assertEquals(commentId, result.parentId());
//        assertEquals(2L, result.userId());
//        // Assert the ArticleDTO
//        assertEquals(article.getId(), result.article().id());
//        // Add more assertions as needed for other fields in the ArticleDTO
//    }


    @Test
    void addReplyToComment_InvalidComment_ThrowsEntityNotFoundException() {
        // Arrange
        Long commentId = 1L;
        CommentDTO replyDTO = new CommentDTO(null, "jane@example.com", "jane@example.com", "This is a reply.", null, 2L, 1L, LocalDateTime.now());

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> commentService.addReplyToComment(commentId, replyDTO));
    }

}
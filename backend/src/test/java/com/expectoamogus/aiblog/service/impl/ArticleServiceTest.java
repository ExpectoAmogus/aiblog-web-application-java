package com.expectoamogus.aiblog.service.impl;

import com.expectoamogus.aiblog.dto.article.ArticleDTO;
import com.expectoamogus.aiblog.dto.mappers.ArticleDTOMapper;
import com.expectoamogus.aiblog.dto.user.UserDTO;
import com.expectoamogus.aiblog.models.Article;
import com.expectoamogus.aiblog.models.User;
import com.expectoamogus.aiblog.service.ArticleFormRepository;
import com.expectoamogus.aiblog.service.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    private ArticleFormRepository articleFormRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ArticleDTOMapper articleDTOMapper;
    @Mock
    private S3Service s3Service;
    @InjectMocks
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findById_ExistingId_ReturnsArticleDTO() {
        // Arrange
        Long id = 1L;
        Article article = new Article();
        ArticleDTO expectedArticleDTO = new ArticleDTO(
                1L, "test", "test", "test", "test", 2L, List.of(),
                new UserDTO(1L, "test", "test@gmail.com", Set.of(new SimpleGrantedAuthority("ROLE_USER")), "test", null, LocalDateTime.now()),
                LocalDateTime.now(),
                0.00
        );
        when(articleFormRepository.findById(id)).thenReturn(Optional.of(article));
        when(articleDTOMapper.apply(article)).thenReturn(expectedArticleDTO);

        // Act
        ArticleDTO result = articleService.findById(id);

        // Assert
        assertThat(result).isEqualTo(expectedArticleDTO);
    }

    @Test
    void findById_NonExistingId_ThrowsEntityNotFoundException() {
        // Arrange
        Long id = 1L;
        when(articleFormRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> articleService.findById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("article with id [%s] not found".formatted(id));
    }

    @Test
    void findAllOrderByDateDesc_ReturnsSortedArticleDTOList() {
        // Arrange
        List<Article> articles = Arrays.asList(new Article(), new Article());
        List<ArticleDTO> expectedArticleDTOs = Arrays.asList(new ArticleDTO(
                1L, "test1", "test1", "test1", "test1", 2L, List.of(),
                new UserDTO(1L, "test", "test@gmail.com", Set.of(new SimpleGrantedAuthority("ROLE_USER")), "test", null, LocalDateTime.now()),
                LocalDateTime.now(),
                0.00
        ), new ArticleDTO(
                2L, "test2", "test2", "test2", "test2", 2L, List.of(),
                new UserDTO(1L, "test", "test@gmail.com", Set.of(new SimpleGrantedAuthority("ROLE_USER")), "test", null, LocalDateTime.now()),
                LocalDateTime.now(),
                0.00
        ));
        PageRequest pageable = PageRequest.of(0, 10);
        when(articleFormRepository.findAll(pageable)).thenReturn(new PageImpl<>(articles));
        when(articleDTOMapper.apply(any(Article.class))).thenReturn(expectedArticleDTOs.get(0), expectedArticleDTOs.get(1));

        // Act
        List<ArticleDTO> result = articleService.findAllOrderByDateDesc(pageable);

        // Assert
        assertThat(result).isEqualTo(expectedArticleDTOs);
    }

    @Test
    void getUserByPrincipal_ExistingUser_ReturnsUser() {
        // Arrange
        Principal principal = () -> "test@gmail.com";
        User expectedUser = new User();
        when(userRepository.findByEmail(principal.getName())).thenReturn(Optional.of(expectedUser));

        // Act
        User result = articleService.getUserByPrincipal(principal);

        // Assert
        assertThat(result).isEqualTo(expectedUser);
    }

    @Test
    void getUserByPrincipal_NonExistingUser_ThrowsUsernameNotFoundException() {
        // Arrange
        Principal principal = () -> "nonexistinguser@gmail.com";
        when(userRepository.findByEmail(principal.getName())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> articleService.getUserByPrincipal(principal))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User does not exists");
    }

//    @Test
//    void findTrending_ReturnsSortedArticleDTOListWithTrendingScore() {
//        // Arrange
//        List<Article> articles = Arrays.asList(new Article(), new Article());
//        List<ArticleDTO> expectedArticleDTOs = Arrays.asList(new ArticleDTO(
//                1L, "test1", "test1", "test1", "test1", 0L, List.of(),
//                new UserDTO(1L, "test", "test@gmail.com", Set.of(new SimpleGrantedAuthority("ROLE_USER")), "test", null, LocalDateTime.now()),
//                LocalDateTime.now(),
//                0.00
//        ), new ArticleDTO(
//                2L, "test2", "test2", "test2", "test2", 0L, List.of(),
//                new UserDTO(1L, "test", "test@gmail.com", Set.of(new SimpleGrantedAuthority("ROLE_USER")), "test", null, LocalDateTime.now()),
//                LocalDateTime.now(),
//                0.00
//        ));
//        PageRequest pageable = PageRequest.of(0, 10);
//        when(articleFormRepository.findAll(pageable)).thenReturn(new PageImpl<>(articles));
//        when(articleDTOMapper.apply(any(Article.class))).thenReturn(expectedArticleDTOs.get(0), expectedArticleDTOs.get(1));
//
//        // Act
//        List<ArticleDTO> result = articleService.findTrending(pageable);
//
//        // Assert
//        assertThat(result).isEqualTo(expectedArticleDTOs);
//    }

    @Test
    void findAllOrderByViewsDesc_ReturnsSortedArticleDTOList() {
        // Arrange
        List<Article> articles = Arrays.asList(new Article(), new Article());
        List<ArticleDTO> expectedArticleDTOs = Arrays.asList(new ArticleDTO(
                1L, "test1", "test1", "test1", "test1", 10L, List.of(),
                new UserDTO(1L, "test", "test@gmail.com", Set.of(new SimpleGrantedAuthority("ROLE_USER")), "test", null, LocalDateTime.now()),
                LocalDateTime.now(),
                0.00
        ), new ArticleDTO(
                2L, "test2", "test2", "test2", "test2", 2L, List.of(),
                new UserDTO(1L, "test", "test@gmail.com", Set.of(new SimpleGrantedAuthority("ROLE_USER")), "test", null, LocalDateTime.now()),
                LocalDateTime.now(),
                0.00
        ));
        PageRequest pageable = PageRequest.of(0, 10);
        when(articleFormRepository.findAll(pageable)).thenReturn(new PageImpl<>(articles));
        when(articleDTOMapper.apply(any(Article.class))).thenReturn(expectedArticleDTOs.get(0), expectedArticleDTOs.get(1));

        // Act
        List<ArticleDTO> result = articleService.findAllOrderByViewsDesc(pageable);

        // Assert
        assertThat(result).isEqualTo(expectedArticleDTOs);
    }

//    @Test
//    void saveArticle_ReturnsSavedArticleDTO() {
//        Long id = 1L;
//        // Arrange
//        Principal principal = mock(Principal.class);
//        String title = "test1";
//        String content = "test1";
//        String category = "test1";
//        List<MultipartFile> files = Arrays.asList(mock(MultipartFile.class), mock(MultipartFile.class));
//
//        Article article = new Article();
//        when(articleFormRepository.save(any(Article.class))).thenReturn(article);
//        ArticleDTO expectedArticleDTO = new ArticleDTO(
//                1L, "test1", "test1", "test1", "test1", 10L, List.of(),
//                new UserDTO(1L, "test", "test@gmail.com", Set.of(new SimpleGrantedAuthority("ROLE_USER")), "test", null, LocalDateTime.now()),
//                LocalDateTime.now(),
//                0.00
//        );
//        when(articleDTOMapper.apply(any(Article.class))).thenReturn(expectedArticleDTO);
//        User existingUser = User.builder()
//                .email("test@gmail.com")
//                .password("testpassword")
//                .isActive(true)
//                .role(Role.ROLE_USER)
//                .build();
//        given(principal.getName()).willReturn("test@gmail.com");
//        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(existingUser));
//
//        // Act
//        ArticleDTO result = articleService.saveArticle(principal, title, content, category, files);
//
//        // Assert
//        assertThat(result).isEqualTo(expectedArticleDTO);
//        verify(articleFormRepository).save(article);
//    }

//    @Test
//    void update_ReturnsUpdatedArticleDTO() {
//        // Arrange
//        Principal principal = mock(Principal.class);
//        Long id = 1L;
//        String title = "Updated Title";
//        String content = "Updated Content";
//        List<MultipartFile> files = Arrays.asList(mock(MultipartFile.class), mock(MultipartFile.class));
//
//        Article article = new Article();
//        when(articleFormRepository.findById(id)).thenReturn(Optional.of(article));
//        when(articleFormRepository.save(any(Article.class))).thenReturn(article);
//        ArticleDTO expectedArticleDTO = new ArticleDTO(
//                1L, "test1", "Updated Title", "Updated Content", "test1", 10L, List.of(),
//                new UserDTO(1L, "test", "test@gmail.com", Set.of(new SimpleGrantedAuthority("ROLE_USER")), "test", null, LocalDateTime.now()),
//                LocalDateTime.now(),
//                0.00
//        );
//        when(articleDTOMapper.apply(any(Article.class))).thenReturn(expectedArticleDTO);
//        User existingUser = User.builder()
//                .email("test@gmail.com")
//                .password("testpassword")
//                .isActive(true)
//                .role(Role.ROLE_USER)
//                .build();
//        given(principal.getName()).willReturn("test@gmail.com");
//        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(existingUser));
//
//        // Act
//        ArticleDTO result = articleService.update(principal, id, title, content, files);
//
//        // Assert
//        assertThat(result).isEqualTo(expectedArticleDTO);
//        verify(articleFormRepository).save(article);
//    }

    @Test
    void deleteById_DeletesArticleAndFiles() {
        // Arrange
        Long id = 1L;
        Article article = mock(Article.class);
        List<String> images = Arrays.asList("image1.jpg", "image2.jpg");
        when(articleFormRepository.findById(id)).thenReturn(Optional.of(article));
        when(article.getImages()).thenReturn(images);

        // Act
        articleService.deleteById(id);

        // Assert
        verify(articleFormRepository).deleteById(id);
        verify(s3Service).deleteFile("image1.jpg");
        verify(s3Service).deleteFile("image2.jpg");
    }

    @Test
    void incrementArticleViews_IncrementsArticleViews() {
        // Arrange
        Long id = 1L;
        Article article = mock(Article.class);
        when(articleFormRepository.findById(id)).thenReturn(Optional.of(article));
        when(article.getViews()).thenReturn(5L);

        // Act
        articleService.incrementArticleViews(id);

        // Assert
        verify(article).setViews(6L);
        verify(articleFormRepository).save(article);
    }
}

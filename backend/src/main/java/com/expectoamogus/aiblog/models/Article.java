package com.expectoamogus.aiblog.models;

import com.expectoamogus.aiblog.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article")
@Getter
@Setter
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "uuid", unique = true)
    private String uuid;
    @Column(name = "title")
    private String title;
    @Column(name = "content", columnDefinition = "text")
    private String content;
    @Column(name = "category")
    private String category;
    @Column(name = "views")
    private Long views;
//    @Column(name = "score")
//    private double trendingScore;
    @ElementCollection(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @CollectionTable(name = "article_images")
    @JoinColumn
    private List<String> images = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    @JsonManagedReference
    private User user;
    private LocalDateTime dateOfCreated;

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }

    public UserDTO getUserDTO() {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getEmail(),
                user.getRole().getAuthorities(),
                user.getUsername(),
                null,
                user.getDateOfCreated());
    }
}

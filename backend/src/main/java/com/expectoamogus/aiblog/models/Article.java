package com.expectoamogus.aiblog.models;

import com.expectoamogus.aiblog.dto.ImageDTO;
import com.expectoamogus.aiblog.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "article")
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "content", columnDefinition = "text")
    private String content;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "article")
    @JsonManagedReference
    private List<Image> images = new ArrayList<>();
    private Long previewImageId;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    @JsonManagedReference
    private User user;
    private LocalDateTime dateOfCreated;

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }

    public void addImagesToArticle(List<Image> images) {
        for (Image image : images) {
            image.setArticle(this);
            this.images.add(image);
        }

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

    public List<ImageDTO> getImageDTOs() {
        return images.stream().map(image ->
                new ImageDTO(
                        image.getId(),
                        image.getName(),
                        image.getOriginalFileName(),
                        image.getSize(),
                        image.getContentType(),
                        image.isPreviewImage(),
                        null))
                .collect(Collectors.toList());
    }
}

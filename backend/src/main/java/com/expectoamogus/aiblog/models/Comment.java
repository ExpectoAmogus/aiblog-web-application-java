package com.expectoamogus.aiblog.models;

import com.expectoamogus.aiblog.dto.article.ArticleDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "text")
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @JsonBackReference
    private Article article;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "parentId")
    private Long parentId;
}

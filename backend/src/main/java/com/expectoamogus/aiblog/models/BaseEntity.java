package com.expectoamogus.aiblog.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateOfCreated;

    @PrePersist
    protected void init() {
        dateOfCreated = LocalDateTime.now();
    }
}

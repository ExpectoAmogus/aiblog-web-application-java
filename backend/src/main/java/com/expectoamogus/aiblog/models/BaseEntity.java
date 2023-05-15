package com.expectoamogus.aiblog.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
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

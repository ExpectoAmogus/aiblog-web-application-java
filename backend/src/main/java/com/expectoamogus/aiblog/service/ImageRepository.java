package com.expectoamogus.aiblog.service;

import com.expectoamogus.aiblog.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}

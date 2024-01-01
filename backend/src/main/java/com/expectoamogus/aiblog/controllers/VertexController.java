package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.service.impl.GptService;
import com.expectoamogus.aiblog.service.impl.VertexService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vertex")
public class VertexController {
    private final VertexService vertexService;

    @PreAuthorize("hasAuthority('devs:write')")
    @GetMapping("/generate-content")
    public String generateResponse(@RequestParam String content) {
        return vertexService.chatDiscussion(content);
    }
}

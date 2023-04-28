package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.utils.ApiUtils;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gpt")
public class GptController {
    private final ApiUtils apiUtils;

    @PreAuthorize("hasAuthority('devs:write')")
    @GetMapping("/generate-content")
    public String generateResponse(@RequestParam String content, HttpServletRequest request) {
        var service = apiUtils.getOpenAiService();
        ChatCompletionResult response = new ChatCompletionResult();
        try {
            response = apiUtils.messageToGPT(apiUtils.getMessages(content, request), service);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.getChoices().get(0).getMessage().getContent();
    }
}

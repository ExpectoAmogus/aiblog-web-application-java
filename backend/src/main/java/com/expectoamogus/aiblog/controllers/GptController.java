package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.utils.ApiUtils;
import com.expectoamogus.aiblog.utils.YamlMessageSource;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gpt")
public class GptController {
    private final ApiUtils apiUtils;
    private static final String MODEL_ID = "gpt-3.5-turbo";
    private static final int MAX_TOKENS = 3000;
    private static final double TEMPERATURE = 0.5;

    public GptController(ApiUtils apiUtils) {
        this.apiUtils = apiUtils;
    }

    @GetMapping("/generate-content")
    public String generateResponse(@RequestParam String content, HttpServletRequest request) {
        var service = apiUtils.getOpenAiService();
        ChatCompletionResult response = new ChatCompletionResult();
        try {
            List<ChatMessage> messages = new ArrayList<>();
            YamlMessageSource messageSource = new YamlMessageSource();
            String systemStringMessage = messageSource.getMessage("system.message",
                    request.getLocale().getLanguage());
            ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemStringMessage);
            messages.add(systemMessage);
            ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), content);
            messages.add(userMessage);
            response = apiUtils.messageToGPT(messages, MODEL_ID, TEMPERATURE, MAX_TOKENS, service);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.getChoices().get(0).getMessage().getContent();
    }
}

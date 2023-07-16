package com.expectoamogus.aiblog.service.impl;

import com.expectoamogus.aiblog.utils.ApiUtils;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GptService {
    private final ApiUtils apiUtils;

    public String getContentFromGPT(String statement, String content, HttpServletRequest request) {
        var service = apiUtils.getOpenAiService();
        ChatCompletionResult response = new ChatCompletionResult();
        try {
            response = apiUtils.messageToGPT(apiUtils.getMessages(statement, content, request), service);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.getChoices().get(0).getMessage().getContent();
    }
}

package com.expectoamogus.aiblog.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.theokanning.openai.service.OpenAiService.*;

@Component
@RequiredArgsConstructor
public class ApiUtils {
    @Value("${ai.token}")
    private String token;
    private final GPTConfig gptConfig;

    public ChatCompletionResult messageToGPT(List<ChatMessage> messages, OpenAiService service) {
        var chatCompletionRequest = ChatCompletionRequest.builder()
                .model(gptConfig.getMODEL_ID())
                .messages(messages)
                .temperature(gptConfig.getTEMPERATURE())
                .maxTokens(gptConfig.getMAX_TOKENS())
                .logitBias(new HashMap<>())
                .build();
        return service.createChatCompletion(chatCompletionRequest);
    }

    @NotNull
    public OpenAiService getOpenAiService() {
        ObjectMapper mapper = defaultObjectMapper();
        OkHttpClient client = defaultClient(token, Duration.ofMinutes(2))
                .newBuilder()
                .build();
        Retrofit retrofit = defaultRetrofit(client, mapper);
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        return new OpenAiService(api);
    }

    @NotNull
    public List<ChatMessage> getMessages(String content, HttpServletRequest request) {
        List<ChatMessage> messages = new ArrayList<>();
        YamlMessageSource messageSource = new YamlMessageSource();
        String systemStringMessage = messageSource.getMessage("system.message",
                request.getLocale().getLanguage());
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemStringMessage);
        messages.add(systemMessage);
        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), content);
        messages.add(userMessage);
        return messages;
    }
}

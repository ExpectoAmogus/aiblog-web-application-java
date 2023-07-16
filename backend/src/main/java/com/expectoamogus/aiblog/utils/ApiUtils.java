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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
    public List<ChatMessage> getMessages(String statement, String content, HttpServletRequest request) {
        List<ChatMessage> messages = new ArrayList<>();
        YamlMessageSource messageSource = new YamlMessageSource();
        String systemStringMessage = "";
        if (Objects.equals(statement, "article")){
            systemStringMessage = messageSource.getMessage("system.message.article",
                    request.getLocale().getLanguage());
        }
        else if (Objects.equals(statement, "trends")){
            systemStringMessage = messageSource.getMessage("system.message.trends",
                    request.getLocale().getLanguage());
        }
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemStringMessage);
        messages.add(systemMessage);
        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), sanitizeContent(content));
        messages.add(userMessage);
        return messages;
    }

    // Implement a method to sanitize titles by removing unsupported characters
    private String sanitizeContent(String content) {
        // Remove unsupported characters and encode the title using RFC 7230 and RFC 3986 standards
        return URLEncoder.encode(content, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20")
                .replaceAll("%21", "!")
                .replaceAll("%27", "'")
                .replaceAll("%28", "(")
                .replaceAll("%29", ")")
                .replaceAll("%7E", "~");
    }
}

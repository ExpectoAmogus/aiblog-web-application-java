package com.expectoamogus.aiblog.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import static com.theokanning.openai.service.OpenAiService.*;

@Component
public class ApiUtils {
    @Value("${ai.token}")
    private String token;

    public ChatCompletionResult messageToGPT(List<ChatMessage> messages, String model_id, double temperature, int max_tokens, OpenAiService service) {
        var chatCompletionRequest = ChatCompletionRequest.builder()
                .model(model_id)
                .messages(messages)
                .temperature(temperature)
                .maxTokens(max_tokens)
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
}

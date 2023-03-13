package com.expectoamogus.aiblog.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.VoiceId;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

@Component
public class ApiUtils {
    @Value("${ai.token}")
    private String token;
    @Value("${aws.access.key}")
    private String accessKey;
    @Value("${aws.secret.key}")
    private String secretKey;
    public void createVoice(AmazonPolly client, ChatCompletionResult response) {
        SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest()
                .withText(response.getChoices().get(0).getMessage().getContent())
                .withVoiceId(VoiceId.Maxim) // Specify the IVONA voice you want to use
                .withOutputFormat(OutputFormat.Mp3);
        SynthesizeSpeechResult synthRes = client.synthesizeSpeech(synthReq);

        try (OutputStream outputStream = new FileOutputStream("output.mp3")) {
            byte[] buffer = new byte[2 * 1024];
            int readBytes;
            while ((readBytes = synthRes.getAudioStream().read(buffer)) > 0) {
                outputStream.write(buffer, 0, readBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChatCompletionResult messageToGPT(List<ChatMessage> messages, String model_id, double temperature, int max_tokens, OpenAiService service) {
        var chatCompletionRequest = ChatCompletionRequest.builder()
                .model(model_id)
                .messages(messages)
                .temperature(temperature)
                .n(2)
                .maxTokens(max_tokens)
                .logitBias(new HashMap<>())
                .build();
        return service.createChatCompletion(chatCompletionRequest);
    }

    @NotNull
    public AmazonPolly getPollyClient() {
        return AmazonPollyClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    @NotNull
    public OpenAiService getOpenAiService() {
        return new OpenAiService(token);
    }
}

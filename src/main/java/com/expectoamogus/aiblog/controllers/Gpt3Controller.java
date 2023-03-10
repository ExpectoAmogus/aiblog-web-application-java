package com.expectoamogus.aiblog.controllers;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.VoiceId;
import com.expectoamogus.aiblog.utils.YamlMessageSource;
import com.expectoamogus.aiblog.dto.ChatMessageDTO;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class Gpt3Controller {
    @Value("${ai.token}")
    private String token;
    @Value("${aws.access.key}")
    private String accessKey;
    @Value("${aws.secret.key}")
    private String secretKey;
    private static final String MODEL_ID = "gpt-3.5-turbo";
    private static final int MAX_TOKENS = 500;
    private static final double TEMPERATURE = 0.7;
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/")
    public String generateResponse(Model model, @ModelAttribute ChatMessageDTO dto, HttpServletRequest request) {
        AmazonPolly client = AmazonPollyClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
        OpenAiService service = new OpenAiService(token);
        try {
            List<ChatMessage> messages = new ArrayList<>();
            YamlMessageSource messageSource = new YamlMessageSource();
            String systemStringMessage = messageSource.getMessage("system.message",
                    request.getLocale().getLanguage());
            ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemStringMessage);
            messages.add(systemMessage);
            ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), dto.content());
            messages.add(userMessage);
            var chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(MODEL_ID)
                    .messages(messages)
                    .temperature(TEMPERATURE)
                    .n(2)
                    .maxTokens(MAX_TOKENS)
                    .logitBias(new HashMap<>())
                    .build();
            var response = service.createChatCompletion(chatCompletionRequest);
            model.addAttribute("message", dto.content());
            model.addAttribute("response", response.getChoices().get(0).getMessage().getContent());

            //createVoice(client, response);
        }catch (Exception e){
            model.addAttribute("response", "Error in communication with OpenAI API.");
            e.printStackTrace();
        }
        return "index";
    }


    private static void createVoice(AmazonPolly client, ChatCompletionResult response) {
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
}

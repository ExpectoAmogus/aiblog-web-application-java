package com.expectoamogus.aiblog.service.impl;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.preview.ChatSession;
import com.google.cloud.vertexai.generativeai.preview.GenerativeModel;
import com.google.cloud.vertexai.generativeai.preview.ResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class VertexService {
    // TODO(developer): Replace these variables before running the sample.
    @Value("${vertex.config.projectId}")
    private String projectId;
    @Value("${vertex.config.region}")
    private String location;
    @Value("${vertex.config.model}")
    private String modelName;

    // Ask interrelated questions in a row using a ChatSession object.
    public String chatDiscussion(String message) {
        // Initialize client that will be used to send requests. This client only needs
        // to be created once, and can be reused for multiple requests.
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
            GenerateContentResponse response;

            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
            // Create a chat session to be used for interactive conversation.
            ChatSession chatSession = new ChatSession(model);

            response = chatSession.sendMessage(message);
            return ResponseHandler.getText(response);
        }
        catch (IOException e) {
            return e.getMessage();
        }
    }
}

package com.expectoamogus.aiblog.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Map;

public class YamlMessageSource {
    private static final String DEFAULT_MESSAGES_FILE = "messages.yml";
    private Map<String, Map<String, String>> messages;

    public YamlMessageSource() {
        this(DEFAULT_MESSAGES_FILE);
    }

    public YamlMessageSource(String messagesFile) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            this.messages = mapper.readValue(new ClassPathResource(messagesFile).getFile(), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(String key, String lang) {
        return messages.getOrDefault(lang, messages.get("en")).get(key);
    }
}

package com.expectoamogus.aiblog.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class GPTConfig {
    @Value("${gpt.config.model}")
    private String MODEL_ID;
    @Value("${gpt.config.tokens}")
    private int MAX_TOKENS;
    @Value("${gpt.config.temperature}")
    private double TEMPERATURE;
}

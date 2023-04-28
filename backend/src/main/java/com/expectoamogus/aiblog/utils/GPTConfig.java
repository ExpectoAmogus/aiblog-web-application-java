package com.expectoamogus.aiblog.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class GPTConfig {
    private final String MODEL_ID = "gpt-3.5-turbo";
    private final int MAX_TOKENS = 3000;
    private final double TEMPERATURE = 0.5;
}

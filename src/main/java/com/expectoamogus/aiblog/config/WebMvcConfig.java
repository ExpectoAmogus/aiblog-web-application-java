package com.expectoamogus.aiblog.config;

import com.expectoamogus.aiblog.utils.MultipartToImageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final MultipartToImageConverter multipartToImageConverter;

    public WebMvcConfig(MultipartToImageConverter multipartToImageConverter) {
        this.multipartToImageConverter = multipartToImageConverter;
    }

    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(multipartToImageConverter);
    }
}

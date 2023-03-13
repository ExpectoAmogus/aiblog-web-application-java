package com.expectoamogus.aiblog.utils;

import com.expectoamogus.aiblog.models.Image;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MultipartToImageConverter implements Converter<MultipartFile, Image> {

    @Override
    public Image convert(MultipartFile multipartFile) {
        try {
            return Image.builder()
                    .name(multipartFile.getName())
                    .originalFileName(multipartFile.getOriginalFilename())
                    .contentType(multipartFile.getContentType())
                    .size(multipartFile.getSize())
                    .bytes(multipartFile.getBytes())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

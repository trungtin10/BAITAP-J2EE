package com.example.bai5_qlsp.config;

import com.example.bai5_qlsp.converter.CategoryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private CategoryConverter categoryConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(categoryConverter);
    }
}

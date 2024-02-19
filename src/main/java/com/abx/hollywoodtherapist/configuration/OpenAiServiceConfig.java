package com.abx.hollywoodtherapist.configuration;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiServiceConfig {
    @Value("${openai.key}")
    private String apiKey;

    @Bean
    public OpenAiService openAiService() {
        OpenAiService service = new OpenAiService(apiKey);
        return service;
    }
}

package com.example.testtaskjustai.config;

import com.example.testtaskjustai.client.VkApiClient;
import com.example.testtaskjustai.client.VkApiClientImpl;
import com.example.testtaskjustai.util.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
@Configuration
@RequiredArgsConstructor
public class RestConfig {
    private final RestTemplateBuilder builder;
    private final VkProperties properties;
    private final LoggingInterceptor interceptor;

    @Bean
    public RestTemplate restTemplate() {
        return builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .additionalInterceptors(interceptor)
                .build();
    }

    @Bean
    public VkApiClient vkApiClient(RestTemplate restTemplate) {
        return new VkApiClientImpl(restTemplate, properties);
    }
}
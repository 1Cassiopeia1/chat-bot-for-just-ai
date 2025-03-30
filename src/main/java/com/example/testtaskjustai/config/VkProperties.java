package com.example.testtaskjustai.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "vk")
public record VkProperties(
        @NotNull Api api,
        @NotNull Bot bot
) {
    public record Api(
            @NotBlank String token,
            String version,
            String baseUrl
    ) {
        public Api {
            version = version != null ? version : "5.131";
            baseUrl = baseUrl != null ? baseUrl : "https://api.vk.com/method";
        }
    }

    public record Bot(
            @NotBlank String confirmationCode,
            @NotNull Integer groupId
    ) {}
}

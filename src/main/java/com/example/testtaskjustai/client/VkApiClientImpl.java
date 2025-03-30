package com.example.testtaskjustai.client;

import com.example.testtaskjustai.config.VkProperties;
import com.example.testtaskjustai.exception.VkApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class VkApiClientImpl implements VkApiClient {
    private final RestTemplate restTemplate;
    private final VkProperties properties;

    @Override
    @Cacheable(value = "vkMessages", key = "{#userId, #message}")
    public void sendMessage(int userId, String message) throws VkApiException {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(properties.api().baseUrl() + "/messages.send")
                    .queryParam("user_id", userId)
                    .queryParam("message", message)
                    .queryParam("access_token", properties.api().token())
                    .queryParam("v", properties.api().version())
                    .queryParam("random_id", System.currentTimeMillis())
                    .build()
                    .toUriString();


            restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            log.error("Failed to send message to user {}", userId, e);
            throw new VkApiException("Failed to send VK message", e);
        }
    }
}

package com.example.testtaskjustai.service;

import com.example.testtaskjustai.client.VkApiClient;
import com.example.testtaskjustai.dto.VkMessage;
import com.example.testtaskjustai.exception.MessageProcessingException;
import com.example.testtaskjustai.exception.VkApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VkMessageService {
    private final VkApiClient vkApiClient;

    @Async
    public void process(VkMessage message) {
        try {
            String responseText = generateResponse(message.getText());
            vkApiClient.sendMessage(message.getFromId(), responseText);
        } catch (VkApiException e) {
            log.error("Failed to send message to user {}", message.getFromId(), e);
            throw new MessageProcessingException("Failed to process message", e);
        }
    }

    public String generateResponse(String text) {
        return "Вы сказали: " + text;
    }
}

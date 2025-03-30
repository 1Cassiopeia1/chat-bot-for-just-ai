package com.example.testtaskjustai.service;

import com.example.testtaskjustai.config.VkProperties;
import com.example.testtaskjustai.dto.VkCallbackRequest;
import com.example.testtaskjustai.dto.VkMessage;
import com.example.testtaskjustai.exception.InvalidRequestException;
import com.example.testtaskjustai.exception.MessageProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VkCallbackService {
    private final VkProperties vkProperties;
    private final VkMessageService messageService;
    private final ObjectMapper objectMapper;

    public String handle(VkCallbackRequest request) {
        if (!request.getGroupId().equals(vkProperties.bot().groupId())) {
            throw new InvalidRequestException("Invalid group ID");
        }

        return switch (request.getType()) {
            case "confirmation" -> vkProperties.bot().confirmationCode();
            case "message_new" -> handleNewMessage(request);
            default -> handleUnknownEvent(request);
        };
    }

    private String handleNewMessage(VkCallbackRequest request) {
        try {
            VkMessage message = objectMapper.convertValue(request.getObject(), VkMessage.class);
            messageService.process(message);
            return "ok";
        } catch (IllegalArgumentException e) {
            throw new MessageProcessingException("Invalid message format", e);
        }
    }

    private String handleUnknownEvent(VkCallbackRequest request) {
        log.warn("Unknown event type: {}", request.getType());
        return "ok";
    }
}

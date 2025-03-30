package com.example.testtaskjustai.serviceTest;

import com.example.testtaskjustai.config.VkProperties;
import com.example.testtaskjustai.dto.VkCallbackRequest;
import com.example.testtaskjustai.dto.VkMessage;
import com.example.testtaskjustai.exception.InvalidRequestException;
import com.example.testtaskjustai.exception.MessageProcessingException;
import com.example.testtaskjustai.service.VkCallbackService;
import com.example.testtaskjustai.service.VkMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VkCallbackServiceTest {

    @Mock
    private VkProperties vkProperties;

    @Mock
    private VkProperties.Bot botProperties;

    @Mock
    private VkMessageService messageService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private VkCallbackService callbackService;

    @BeforeEach
    void setUp() {
        when(vkProperties.bot()).thenReturn(botProperties);
    }

    @Test
    void handle_shouldProcessMessage_WhenMessageNewType() {
        // Arrange
        VkCallbackRequest request = new VkCallbackRequest();
        request.setType("message_new");
        request.setGroupId(123);
        request.setObject(Map.of("from_id", 123, "text", "hi", "peer_id", 123));

        VkMessage expectedMessage = new VkMessage(123, "hi", 123);

        when(botProperties.groupId()).thenReturn(123);
        when(objectMapper.convertValue(any(), eq(VkMessage.class))).thenReturn(expectedMessage);

        // Act
        String result = callbackService.handle(request);

        // Assert
        assertEquals("ok", result);
        verify(messageService).process(expectedMessage);
    }

    @Test
    void handle_shouldReturnConfirmationCode_WhenConfirmationType() {
        // Arrange
        VkCallbackRequest request = new VkCallbackRequest();
        request.setType("confirmation");
        request.setGroupId(123);

        when(botProperties.groupId()).thenReturn(123);
        when(botProperties.confirmationCode()).thenReturn("confirm_code");

        // Act
        String result = callbackService.handle(request);

        // Assert
        assertEquals("confirm_code", result);
    }

    @Test
    void handle_shouldReturnOk_WhenUnknownType() {
        // Arrange
        VkCallbackRequest request = new VkCallbackRequest();
        request.setType("unknown_type");
        request.setGroupId(123);

        when(botProperties.groupId()).thenReturn(123);

        // Act
        String result = callbackService.handle(request);

        // Assert
        assertEquals("ok", result);
    }

    @Test
    void handle_shouldThrowException_WhenInvalidGroupId() {
        // Arrange
        VkCallbackRequest request = new VkCallbackRequest();
        request.setType("message_new");
        request.setGroupId(456); // Не совпадает с ожидаемым

        when(botProperties.groupId()).thenReturn(123);

        // Act & Assert
        assertThrows(InvalidRequestException.class,
                () -> callbackService.handle(request));
    }

    @Test
    void handle_shouldThrowException_WhenInvalidMessageFormat() {
        // Arrange
        VkCallbackRequest request = new VkCallbackRequest();
        request.setType("message_new");
        request.setGroupId(123);
        request.setObject("invalid_object");

        when(botProperties.groupId()).thenReturn(123);
        when(objectMapper.convertValue(any(), eq(VkMessage.class)))
                .thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(MessageProcessingException.class,
                () -> callbackService.handle(request));
    }
}
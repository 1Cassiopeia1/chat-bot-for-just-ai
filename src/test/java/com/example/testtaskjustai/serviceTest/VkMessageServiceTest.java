package com.example.testtaskjustai.serviceTest;

import com.example.testtaskjustai.client.VkApiClient;
import com.example.testtaskjustai.dto.VkMessage;
import com.example.testtaskjustai.exception.MessageProcessingException;
import com.example.testtaskjustai.exception.VkApiException;
import com.example.testtaskjustai.service.VkMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VkMessageServiceTest {

    @Mock
    private VkApiClient vkApiClient;

    @InjectMocks
    private VkMessageService messageService;

    @Test
    void process_shouldSendResponse_WhenValidMessage() throws VkApiException {
        // Arrange
        VkMessage message = new VkMessage(123, "test message", 123);
        doNothing().when(vkApiClient).sendMessage(anyInt(), anyString());

        // Act & Assert
        assertDoesNotThrow(() -> messageService.process(message));
        verify(vkApiClient).sendMessage(123, "Вы сказали: test message");
    }

    @Test
    void process_shouldThrowException_WhenApiFails() throws VkApiException {
        // Arrange
        VkMessage message = new VkMessage(123, "test", 123);
        doThrow(new VkApiException("API error"))
                .when(vkApiClient).sendMessage(anyInt(), anyString());

        // Act & Assert
        assertThrows(MessageProcessingException.class,
                () -> messageService.process(message));
    }

    @Test
    void generateResponse_shouldReturnFormattedString() {
        // Arrange
        String input = "Hello";

        // Act
        String result = messageService.generateResponse(input);

        // Assert
        assertEquals("Вы сказали: Hello", result);
    }

    @Test
    void generateResponse_shouldHandleEmptyMessage() {
        // Arrange
        String input = "";

        // Act
        String result = messageService.generateResponse(input);

        // Assert
        assertEquals("Вы сказали: ", result);
    }
}
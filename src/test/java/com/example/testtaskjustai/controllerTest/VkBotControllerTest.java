package com.example.testtaskjustai.controllerTest;

import com.example.testtaskjustai.controller.VkBotController;
import com.example.testtaskjustai.dto.VkCallbackRequest;
import com.example.testtaskjustai.exception.InvalidRequestException;
import com.example.testtaskjustai.service.VkCallbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VkBotController.class)
class VkBotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VkCallbackService callbackService;

    @Test
    void handleCallback_shouldReturnConfirmationCode() throws Exception {
        // Arrange
        VkCallbackRequest request = new VkCallbackRequest();
        request.setType("confirmation");
        request.setGroupId(123);

        when(callbackService.handle(any(VkCallbackRequest.class)))
                .thenReturn("confirm_12345");

        // Act & Assert
        mockMvc.perform(post("/api/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("confirm_12345"));

        verify(callbackService, times(1)).handle(any());
    }

    @Test
    void handleCallback_shouldReturnOkForMessage() throws Exception {
        // Arrange
        VkCallbackRequest request = new VkCallbackRequest();
        request.setType("message_new");
        request.setGroupId(123);

        when(callbackService.handle(any(VkCallbackRequest.class)))
                .thenReturn("ok");

        // Act & Assert
        mockMvc.perform(post("/api/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void handleCallback_shouldReturnOkForUnknownEvent() throws Exception {
        // Arrange
        VkCallbackRequest request = new VkCallbackRequest();
        request.setType("unknown_event");
        request.setGroupId(123);

        when(callbackService.handle(any(VkCallbackRequest.class)))
                .thenReturn("ok");

        // Act & Assert
        mockMvc.perform(post("/api/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void handleCallback_shouldReturn400ForInvalidRequest() throws Exception {
        // Arrange
        VkCallbackRequest request = new VkCallbackRequest();
        request.setType("message_new");
        request.setGroupId(999); // Invalid group

        when(callbackService.handle(any(VkCallbackRequest.class)))
                .thenThrow(new InvalidRequestException("Invalid group ID"));

        // Act & Assert
        mockMvc.perform(post("/api/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid group ID"));
    }

    @Test
    void handleCallback_shouldReturn500ForServerError() throws Exception {
        // Arrange
        VkCallbackRequest request = new VkCallbackRequest();
        request.setType("message_new");
        request.setGroupId(123);

        when(callbackService.handle(any(VkCallbackRequest.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(post("/api/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("server error"));
    }

}

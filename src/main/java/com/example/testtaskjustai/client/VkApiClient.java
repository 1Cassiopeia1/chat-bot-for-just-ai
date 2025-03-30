package com.example.testtaskjustai.client;

import com.example.testtaskjustai.exception.VkApiException;

public interface VkApiClient {
    void sendMessage(int userId, String message) throws VkApiException;
}

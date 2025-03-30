package com.example.testtaskjustai.util;

import com.example.testtaskjustai.exception.VkApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
public class VkApiErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.error("VK API error: {} {}", response.getStatusCode(), response.getStatusText());
        try {
            throw new VkApiException("VK API returned error: " + response.getStatusCode());
        } catch (VkApiException e) {
            e.printStackTrace();
        }
    }
}

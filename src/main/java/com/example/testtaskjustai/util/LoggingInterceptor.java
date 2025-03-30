package com.example.testtaskjustai.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        // Логируем исходящий запрос
        log.debug("Outgoing request: {} {}", request.getMethod(), request.getURI());

        // Логируем тело запроса (если нужно)
        if (log.isTraceEnabled()) {
            log.trace("Request body: {}", new String(body, StandardCharsets.UTF_8));
        }

        // Продолжаем выполнение запроса
        ClientHttpResponse response = execution.execute(request, body);

        // Логируем ответ
        log.debug("Response status: {}", response.getStatusCode());

        return response;
    }
}

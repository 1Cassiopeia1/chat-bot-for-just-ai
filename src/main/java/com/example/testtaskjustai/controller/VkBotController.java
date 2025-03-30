package com.example.testtaskjustai.controller;

import com.example.testtaskjustai.dto.VkCallbackRequest;
import com.example.testtaskjustai.exception.InvalidRequestException;
import com.example.testtaskjustai.service.VkCallbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/callback")
@RequiredArgsConstructor
@EnableAsync
public class VkBotController {
    private final VkCallbackService callbackService;

    @PostMapping
    public ResponseEntity<String> handleCallback(@Valid @RequestBody VkCallbackRequest request) {
        try {
            return ResponseEntity.ok(callbackService.handle(request));
        } catch (InvalidRequestException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error", e);
            return ResponseEntity.internalServerError().body("server error");
        }
    }
}

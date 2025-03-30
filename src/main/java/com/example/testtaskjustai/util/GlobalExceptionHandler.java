package com.example.testtaskjustai.util;

import com.example.testtaskjustai.exception.InvalidRequestException;
import com.example.testtaskjustai.exception.MessageProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequest(InvalidRequestException ex) {
        log.warn("Invalid request: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(MessageProcessingException.class)
    public ResponseEntity<String> handleMessageProcessing(MessageProcessingException ex) {
        log.error("Message processing failed", ex);
        return ResponseEntity.internalServerError().body("message processing failed");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.internalServerError().body("server error");
    }
}

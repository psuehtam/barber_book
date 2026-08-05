package com.barberbook.backend.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
        LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    ResponseEntity<ApiErrorResponse> handleApiException(
        ApiException exception,
        HttpServletRequest request
    ) {
        return build(
            exception.getStatus(),
            exception.getCode(),
            exception.getMessage(),
            request.getRequestURI(),
            Map.of()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> handleValidation(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        Map<String, String> fields = new LinkedHashMap<>();

        exception.getBindingResult()
            .getFieldErrors()
            .forEach(error ->
                fields.putIfAbsent(
                    error.getField(),
                    error.getDefaultMessage()
                )
            );

        return build(
            HttpStatus.BAD_REQUEST,
            "VALIDATION_ERROR",
            "Existem campos inválidos.",
            request.getRequestURI(),
            fields
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiErrorResponse> handleMalformedJson(
        HttpMessageNotReadableException exception,
        HttpServletRequest request
    ) {
        return build(
            HttpStatus.BAD_REQUEST,
            "MALFORMED_JSON",
            "O JSON enviado está inválido.",
            request.getRequestURI(),
            Map.of()
        );
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiErrorResponse> handleUnexpected(
        Exception exception,
        HttpServletRequest request
    ) {
        log.error(
            "Unexpected error on {}",
            request.getRequestURI(),
            exception
        );

        return build(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "INTERNAL_ERROR",
            "Ocorreu um erro interno.",
            request.getRequestURI(),
            Map.of()
        );
    }

    private ResponseEntity<ApiErrorResponse> build(
        HttpStatus status,
        String code,
        String message,
        String path,
        Map<String, String> fields
    ) {
        ApiErrorResponse body = new ApiErrorResponse(
            Instant.now(),
            status.value(),
            code,
            message,
            path,
            fields
        );

        return ResponseEntity.status(status).body(body);
    }
}

package com.barberbook.backend.exception;

import com.barberbook.backend.domain.user.EmailAlreadyRegisteredException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleEmailAlreadyRegistered(
        EmailAlreadyRegisteredException exception,
        HttpServletRequest request
    ) {
        return new ApiErrorResponse(
            Instant.now(),
            HttpStatus.CONFLICT.value(),
            "EMAIL_ALREADY_REGISTERED",
            exception.getMessage(),
            request.getRequestURI(),
            Map.of()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidation(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        exception.getBindingResult()
            .getFieldErrors()
            .forEach(error ->
                fieldErrors.putIfAbsent(
                    error.getField(),
                    error.getDefaultMessage()
                )
            );

        return new ApiErrorResponse(
            Instant.now(),
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            "Existem campos inválidos na requisição.",
            request.getRequestURI(),
            fieldErrors
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleMalformedJson(
        HttpMessageNotReadableException exception,
        HttpServletRequest request
    ) {
        return new ApiErrorResponse(
            Instant.now(),
            HttpStatus.BAD_REQUEST.value(),
            "MALFORMED_JSON",
            "O JSON enviado está inválido.",
            request.getRequestURI(),
            Map.of()
        );
    }
}

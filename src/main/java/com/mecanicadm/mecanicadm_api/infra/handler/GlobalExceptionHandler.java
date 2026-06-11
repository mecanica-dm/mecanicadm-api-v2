package com.mecanicadm.mecanicadm_api.infra.handler;

import com.mecanicadm.mecanicadm_api.core.user.exception.UserExceptions;
import com.mecanicadm.mecanicadm_api.infra.exception.SecurityException;
import com.mecanicadm.mecanicadm_api.shared.exception.DomainExceptionCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_FIELD_NAME = "error";
    private static final String DEFAULT_VALIDATION_MESSAGE = "Erro de validação";

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(UserExceptions.BadCredentials.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(UserExceptions.BadCredentials ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), ex.getMessageKey(), locale);
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_FIELD_NAME, message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(DomainExceptionCore.class)
    public ResponseEntity<Map<String, String>> handleDomainException(DomainExceptionCore ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), ex.getMessageKey(), locale);
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_FIELD_NAME, message);
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, String>> handleSecurityException(SecurityException ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessage(), null, ex.getMessage(), locale);
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_FIELD_NAME, message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException ex, Locale locale) {
        String message = messageSource.getMessage("error.bad.credentials", null, locale);
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_FIELD_NAME, message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage() != null ? error.getDefaultMessage() : DEFAULT_VALIDATION_MESSAGE;
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException() {
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_FIELD_NAME, "Ocorreu um erro interno no servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

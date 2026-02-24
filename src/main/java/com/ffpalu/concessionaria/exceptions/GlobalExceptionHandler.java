package com.ffpalu.concessionaria.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomerException.class, SaleException.class, SellerException.class, UserException.class, VehicleException.class})
    public ResponseEntity<Map<String, Object>> handleNotFoundException(RuntimeException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("timestamp", LocalDateTime.now());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredential(BadCredentialsException e) {
        Map<String, Object> error = new HashMap<>();

        error.put("message", e.getMessage());
        error.put("status", HttpStatus.UNAUTHORIZED.value());
        error.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException e){
        Map<String, String> fieldErrors = new HashMap<>();


        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Errori di validazione");
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());
        response.put("errors", fieldErrors);

        return ResponseEntity.badRequest().body(response);

    }



}

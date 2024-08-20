package com.berru.app.ecommercespringboot.exception;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Bad Request");

        // İlk hata mesajını al
        String defaultMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        errorResponse.put("message", defaultMessage);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", "Not Found");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        String message = "A record with the same name already exists. Please choose a different name.";
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", message);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
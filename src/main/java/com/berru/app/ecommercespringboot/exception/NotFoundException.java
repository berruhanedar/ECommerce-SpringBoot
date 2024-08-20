package com.berru.app.ecommercespringboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
// Bu anotasyon, bu istisna fırlatıldığında HTTP 404 kodu döndürüleceğini belirtir.
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
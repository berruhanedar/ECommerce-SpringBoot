package com.berru.app.ecommercespringboot.exception;

public class NotFoundException extends BadRequestException {

    public NotFoundException(String message) {
        super(message);
    }
}
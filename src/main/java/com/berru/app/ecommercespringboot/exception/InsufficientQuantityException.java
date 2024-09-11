package com.berru.app.ecommercespringboot.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class InsufficientQuantityException extends BaseException {

    public InsufficientQuantityException(String message) {
        super(message);
    }

    public InsufficientQuantityException(String message, List<? extends ErrorDetailDto> errors) {
        super(message, errors);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}

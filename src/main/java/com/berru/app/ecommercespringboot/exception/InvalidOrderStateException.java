package com.berru.app.ecommercespringboot.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidOrderStateException extends BaseException {

    public InvalidOrderStateException(String message) {
        super(message);
    }

    public InvalidOrderStateException(String message, List<? extends ErrorDetailDto> errors) {
        super(message, errors);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST; // Veya uygun olan HTTP durum kodunu belirleyin.
    }
}

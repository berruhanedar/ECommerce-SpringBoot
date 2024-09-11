package com.berru.app.ecommercespringboot.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class InsufficientBalanceException extends BaseException {

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, List<? extends ErrorDetailDto> errors) {
        super(message, errors);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}

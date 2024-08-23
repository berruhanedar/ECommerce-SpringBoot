package com.berru.app.ecommercespringboot.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class BaseException extends RuntimeException implements IBaseException {

    @Serial
    private static final long serialVersionUID = 6924148108456983634L;

    protected List<? extends ErrorDetailDto> errors = new ArrayList<>();

    @Getter
    @Setter
    private String code;

    BaseException(String message) {
        super(message);
        this.code = this.getClass().getSimpleName();
    }

    BaseException(String message, List<? extends ErrorDetailDto> errors) {
        super(message, null, false, false);
        this.errors = errors;
        this.code = this.getClass().getSimpleName();
    }

    @JsonIgnore
    public List getErrorsInternal() {
        return this.errors;
    }
}

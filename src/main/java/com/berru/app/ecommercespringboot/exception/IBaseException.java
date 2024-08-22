package com.berru.app.ecommercespringboot.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(
        {"cause", "detailMessage", "localizedMessage", "stackTrace", "suppressed", "suppressedExceptions", "type"}
)
@JsonPropertyOrder({"type", "message", "errors", "code"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface IBaseException {

    String getMessage();

    @JsonIgnore
    HttpStatus getHttpStatus();

    @JsonInclude
    String getCode();

    @JsonIgnore
    List getErrorsInternal();

    default String toString(String toString) {
        if (getErrorsInternal() != null && !getErrorsInternal().isEmpty()) {
            return toString + ": " + Arrays.toString(getErrorsInternal().toArray());
        }
        return toString;
    }

}

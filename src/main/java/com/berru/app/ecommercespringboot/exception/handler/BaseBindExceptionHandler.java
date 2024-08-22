package com.berru.app.ecommercespringboot.exception.handler;

import com.berru.app.ecommercespringboot.exception.BadRequestException;
import com.berru.app.ecommercespringboot.exception.ErrorDetailDto;
import com.berru.app.ecommercespringboot.exception.IBaseException;
import com.berru.app.ecommercespringboot.exception.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Comparator;
import java.util.List;

@Slf4j
public abstract class BaseBindExceptionHandler {

    public ResponseEntity<IBaseException> processBindingResult(BindingResult result) {
        List<ObjectError> errors = result.getAllErrors();

        List<ErrorDetailDto> badRequestErrorDtos = errors.stream()
                .map(this::getDtoFromError)
                .sorted(Comparator.comparing(ErrorDetailDto::getMessage))
                .toList();

        BadRequestException badRequestException = new BadRequestException("Validation failed", badRequestErrorDtos);

        return new ResponseEntity<>(badRequestException, badRequestException.getHttpStatus());
    }

    private ErrorDetailDto getDtoFromError(ObjectError error) {
        String message = error.getDefaultMessage();
        String objectName = error.getObjectName();

        if (error instanceof FieldError fieldError) {
            return getDtoFromFieldError(fieldError.getField(), message);
        }
        return getDtoFromObjectError(objectName, message);
    }

    private ErrorDetailDto getDtoFromFieldError(String field, String message) {
        return ErrorDetailDto.builder()
                .type(MessageType.VALIDATION)
                .code(field)
                .message(message)
                .build();
    }

    private ErrorDetailDto getDtoFromObjectError(String objectName, String message) {
        return ErrorDetailDto.builder()
                .type(MessageType.BUSINESS)
                .code(objectName)
                .message(message)
                .build();
    }
}

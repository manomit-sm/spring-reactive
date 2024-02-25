package com.bsolz.webfluxdemo.exceptions;

import com.bsolz.webfluxdemo.dtos.InputFailedValidationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InputValidationExceptionHandler {

    @ExceptionHandler(InputValidationException.class)
    public ResponseEntity<InputFailedValidationResponse> handleException(InputValidationException ex) {
        return ResponseEntity.badRequest()
                .body(new InputFailedValidationResponse(ex.getErrorCode(), ex.getInput(), ex.getMessage()));
    }
}

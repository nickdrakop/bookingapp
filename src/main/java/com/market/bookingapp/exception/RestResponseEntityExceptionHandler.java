package com.market.bookingapp.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ApplicationException.class })
    protected ResponseEntity<Object> handleApplicationException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ((ApplicationException) ex).getAppError().getDescription(),
            new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
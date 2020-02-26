package com.alexquasar.authenticationService.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Throwable.class})
    protected ResponseEntity<Object> handleConflict(Throwable ex, WebRequest request) {
        return handleExceptionInternal(new RuntimeException(), ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {ServiceException.class})
    protected ResponseEntity<Object> handleConflict(ServiceException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(value = {ResponseStatusException.class})
    protected ResponseEntity<Object> handleConflict(ResponseStatusException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), ex.getStatus(), request);
    }
}

package org.lxp.springboot.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handleError(Exception exception, HttpServletResponse response) throws IOException {
        log.error(exception.getMessage(), exception);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        final var error = ex.getBindingResult().getFieldErrors().getFirst();
        final var errorMap = new HashMap<>();
        errorMap.put("field", error.getField());
        errorMap.put("value", error.getRejectedValue());
        errorMap.put("reason", error.getDefaultMessage());
        return new ResponseEntity<>(errorMap, status);
    }
}

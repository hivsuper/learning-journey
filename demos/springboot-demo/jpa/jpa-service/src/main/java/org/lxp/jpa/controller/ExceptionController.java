package org.lxp.jpa.controller;

import lombok.extern.slf4j.Slf4j;
import org.lxp.jpa.response.RtnResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RtnResponse> handleError(Exception exception) {
        log.error(exception.getMessage(), exception);
        final var rtn = new RtnResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
        return ResponseEntity.internalServerError().body(rtn);
    }
}

package org.lxp.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.lxp.response.RtnResponse;
import org.lxp.weixin.exception.WXException;
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
    public ResponseEntity<RtnResponse> handleError(Exception exception, HttpServletResponse response) {
        log.error(exception.getMessage(), exception);
        RtnResponse rtn = new RtnResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
        return ResponseEntity.internalServerError().body(rtn);
    }

    @ExceptionHandler(WXException.class)
    public ResponseEntity<RtnResponse> handleWXException(WXException exception, HttpServletResponse response) {
        log.error(exception.getMessage(), exception);
        RtnResponse rtn = new RtnResponse(exception.getErrorCode(), exception.getMessage());
        return ResponseEntity.internalServerError().body(rtn);
    }
}

package org.lxp.weixin.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.lxp.weixin.admin.exception.AdminException;
import org.lxp.weixin.response.RtnResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.lxp.weixin.exception.ErrorCodeEnum.ADMIN_CREDENTIAL_MISMATCHED;
import static org.lxp.weixin.exception.ErrorCodeEnum.SERVER_ERROR;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RtnResponse> handleError(Exception exception) {
        log.error(exception.getMessage(), exception);
        final var rtn = new RtnResponse(SERVER_ERROR.code, exception.getMessage());
        return ResponseEntity.internalServerError().body(rtn);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RtnResponse> handleBadCredentialsException(BadCredentialsException exception) {
        log.error(exception.getMessage(), exception);
        final var errorCode = ADMIN_CREDENTIAL_MISMATCHED.code;
        final var rtn = new RtnResponse(errorCode, exception.getMessage());
        final var statusCode = String.valueOf(errorCode).substring(0, 3);
        return ResponseEntity.status(Integer.valueOf(statusCode)).body(rtn);
    }

    @ExceptionHandler(AdminException.class)
    public ResponseEntity<RtnResponse> handleAdminException(AdminException exception) {
        log.error(exception.getMessage(), exception);
        final var rtn = new RtnResponse(exception.getErrorCode(), exception.getMessage());
        final var statusCode = String.valueOf(exception.getErrorCode()).substring(0, 3);
        return ResponseEntity.status(Integer.valueOf(statusCode)).body(rtn);
    }
}

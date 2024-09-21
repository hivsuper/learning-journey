package org.lxp.weixin.admin.exception;

import lombok.Getter;
import org.lxp.weixin.exception.ErrorCodeEnum;

@Getter
public class AdminException extends RuntimeException {
    private Integer errorCode;

    public AdminException(ErrorCodeEnum errorCode) {
        this(errorCode.code, errorCode.message);
    }

    public AdminException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

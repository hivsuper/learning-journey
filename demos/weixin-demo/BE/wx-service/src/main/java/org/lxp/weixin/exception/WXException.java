package org.lxp.weixin.exception;

import lombok.Getter;

@Getter
public class WXException extends RuntimeException {
    private Integer errorCode;

    public WXException(ErrorCodeEnum errorCode) {
        this(errorCode.code, errorCode.message);
    }

    private WXException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

package org.lxp.weixin.exception;

import lombok.Getter;

@Getter
public class WXException extends RuntimeException {
    private Integer errorCode;

    public WXException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

package org.lxp.weixin.exception;

public enum ErrorCodeEnum {
    WX_FAILURE(4001, "WX Failure"),
    ADMIN_NOT_FOUND(4041, "Admin Not Found"),
    SERVER_ERROR(5000, "Server Error"),
    PASSWORD_MISMATCHED(5001, "Password Mismatched");

    public int code;
    public String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

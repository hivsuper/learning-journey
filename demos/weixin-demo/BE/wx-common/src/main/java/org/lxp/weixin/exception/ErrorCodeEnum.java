package org.lxp.weixin.exception;

public enum ErrorCodeEnum {
    WX_FAILURE(4001, "WX Failure"),
    USER_FAILED_LOGIN(4001000, "WeChat User Failed to Login"),
    USER_NOT_LOGIN(4011000, "User Not Login"),
    USER_NOT_AUTHENTICATED(4031000, "User Not Authenticated"),
    SERVER_ERROR(5000, "Server Error"),

    ADMIN_CREDENTIAL_MISMATCHED(4001001, "Admin Not Found"),
    ADMIN_NOT_LOGIN(4011000, "Admin Not Login"),
    ADMIN_NOT_AUTHENTICATED(4031000, "Admin Not Authenticated"),
    ADMIN_NOT_FOUND(4041001, "Admin Not Found");

    public int code;
    public String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

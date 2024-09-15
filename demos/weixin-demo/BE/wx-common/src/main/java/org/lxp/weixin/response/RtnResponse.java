package org.lxp.weixin.response;

public record RtnResponse<T>(int code, T data) {
}
package org.lxp.response;

public record RtnResponse<T>(int code, T data) {
}
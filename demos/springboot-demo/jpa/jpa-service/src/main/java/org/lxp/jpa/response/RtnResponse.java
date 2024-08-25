package org.lxp.jpa.response;

public record RtnResponse<T>(int code, T data) {
}
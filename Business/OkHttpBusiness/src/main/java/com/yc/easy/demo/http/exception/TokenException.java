package com.yc.easy.demo.http.exception;


import com.yc.http.exception.NetHttpException;

public final class TokenException extends NetHttpException {

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
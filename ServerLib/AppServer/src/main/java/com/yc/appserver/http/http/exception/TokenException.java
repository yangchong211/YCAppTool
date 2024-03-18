package com.yc.appserver.http.http.exception;


import com.yc.http.synthesis.exception.NetHttpException;

public final class TokenException extends NetHttpException {

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
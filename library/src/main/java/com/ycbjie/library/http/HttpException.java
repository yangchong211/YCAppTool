package com.ycbjie.library.http;

/**
 * @author yc
 */
public class HttpException extends Exception {

    private int code;
    private String displayMessage;

    public HttpException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public int getCode() {
        return code;
    }
}


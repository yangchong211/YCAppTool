package com.yc.http.easy;

/**
 * @author yc
 */
public class HttpException extends Exception {

    private String displayMessage;

    public HttpException(Throwable throwable) {
        super(throwable);
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

}


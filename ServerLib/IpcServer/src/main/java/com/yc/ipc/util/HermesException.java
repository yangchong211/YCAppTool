

package com.yc.ipc.util;

/**
 * Created by yangchong on 16/4/15.
 */
public class HermesException extends Exception {

    private int mErrorCode;

    private String mErrorMessage;

    public HermesException(int errorCode, String errorMessage) {
        mErrorCode = errorCode;
        mErrorMessage = errorMessage;
    }

    public HermesException(int errorCode, String errorMessage, Throwable t) {
        super(t);
        mErrorCode = errorCode;
        mErrorMessage = errorMessage;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
}

package com.yc.okhttp.okhttp;

import android.text.TextUtils;

/**
 * Url 无效的异常
 */
public class InvalidUrlException extends RuntimeException {

    public InvalidUrlException(String url) {
        super("You've configured an invalid url : " + (TextUtils.isEmpty(url) ? "EMPTY_OR_NULL_URL" : url));
    }
}

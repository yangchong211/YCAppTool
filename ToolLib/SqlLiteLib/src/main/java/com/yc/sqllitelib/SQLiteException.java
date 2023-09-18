package com.yc.sqllitelib;


import java.io.IOException;

/**
 * sql异常
 */
public class SQLiteException extends IOException {

    public SQLiteException(String message) {
        super(message);
    }

}

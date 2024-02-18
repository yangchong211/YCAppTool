package me.pqpo.librarylog4a.interceptor;


import me.pqpo.librarylog4a.LogData;

/**
 * Created by pqpo on 2017/11/21.
 */
public interface Interceptor {
    boolean intercept(LogData logData);
}

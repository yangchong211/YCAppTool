package me.pqpo.librarylog4a.interceptor;


import me.pqpo.librarylog4a.LogData;


public interface Interceptor {
    boolean intercept(LogData logData);
}

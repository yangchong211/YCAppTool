package me.pqpo.librarylog4a.inter;


import me.pqpo.librarylog4a.LogPoolData;


public interface Interceptor {
    boolean intercept(LogPoolData logData);
}

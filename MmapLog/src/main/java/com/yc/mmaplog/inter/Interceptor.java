package com.yc.mmaplog.inter;


import com.yc.mmaplog.LogPoolData;


public interface Interceptor {
    boolean intercept(LogPoolData logData);
}

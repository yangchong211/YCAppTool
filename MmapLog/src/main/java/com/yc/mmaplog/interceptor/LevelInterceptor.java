package com.yc.mmaplog.interceptor;

import com.yc.mmaplog.LogLevel;
import com.yc.mmaplog.LogPoolData;
import com.yc.mmaplog.inter.Interceptor;


public class LevelInterceptor implements Interceptor {

    private int level = LogLevel.VERBOSE;

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean intercept(LogPoolData logData) {
        return logData != null && logData.logLevel >= level;
    }
}

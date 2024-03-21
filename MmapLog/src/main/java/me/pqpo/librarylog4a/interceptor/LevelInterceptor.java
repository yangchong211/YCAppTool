package me.pqpo.librarylog4a.interceptor;

import me.pqpo.librarylog4a.LogLevel;
import me.pqpo.librarylog4a.LogPoolData;
import me.pqpo.librarylog4a.inter.Interceptor;


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

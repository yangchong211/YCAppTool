package me.pqpo.librarylog4a.interceptor;

import me.pqpo.librarylog4a.Level;
import me.pqpo.librarylog4a.LogData;

/**
 * Created by pqpo on 2017/11/21.
 */
public class LevelInterceptor implements Interceptor {

    private int level = Level.VERBOSE;

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean intercept(LogData logData) {
        return logData != null && logData.logLevel >= level;
    }
}

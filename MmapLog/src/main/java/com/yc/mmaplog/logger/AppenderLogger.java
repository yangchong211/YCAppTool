package com.yc.mmaplog.logger;

import java.util.ArrayList;
import java.util.List;

import com.yc.mmaplog.inter.InterAppender;
import com.yc.mmaplog.inter.InterLogger;


public class AppenderLogger implements InterLogger {

    private final List<InterAppender> appenderList = new ArrayList<>();

    protected AppenderLogger() {
    }

    public List<InterAppender> getAppenderList() {
        return appenderList;
    }

    public void addAppender(InterAppender appender) {
        if (appender != null) {
            appenderList.add(appender);
        }
    }

    @Override
    public void println(int priority, String tag, String msg) {
        for (InterAppender appender : appenderList) {
            appender.append(priority, tag, msg);
        }
    }

    @Override
    public void flush() {
        for (InterAppender appender : appenderList) {
            appender.flush();
        }
    }

    @Override
    public void release() {
        for (InterAppender appender : appenderList) {
            appender.release();
        }
        appenderList.clear();
    }


    public static class Builder {

        private final AppenderLogger logger;

        public Builder() {
            logger = new AppenderLogger();
        }

        public Builder addAppender(InterAppender appender) {
            logger.addAppender(appender);
            return this;
        }

        public AppenderLogger create() {
            return logger;
        }

    }

}

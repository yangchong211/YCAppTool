package me.pqpo.librarylog4a.logger;

import java.util.ArrayList;
import java.util.List;

import me.pqpo.librarylog4a.appender.Appender;


public class AppenderLogger implements Logger{

    private final List<Appender> appenderList = new ArrayList<>();

    protected AppenderLogger() {
    }

    public List<Appender> getAppenderList() {
        return appenderList;
    }

    public void addAppender(Appender appender) {
        if (appender != null) {
            appenderList.add(appender);
        }
    }

    @Override
    public void println(int priority, String tag, String msg) {
        for (Appender appender : appenderList) {
            appender.append(priority, tag, msg);
        }
    }

    @Override
    public void flush() {
        for (Appender appender : appenderList) {
            appender.flush();
        }
    }

    @Override
    public void release() {
        for (Appender appender : appenderList) {
            appender.release();
        }
        appenderList.clear();
    }


    public static class Builder {

        private final AppenderLogger logger;

        public Builder() {
            logger = new AppenderLogger();
        }

        public Builder addAppender(Appender appender) {
            logger.addAppender(appender);
            return this;
        }

        public AppenderLogger create() {
            return logger;
        }

    }

}

package com.yc.library;

import com.yc.eventuploadlib.LoggerReporter;
import com.yc.toolutils.AppLogUtils;

public class LoggerReporterImpl extends LoggerReporter {
    @Override
    protected void reportLog(String log) {
        AppLogUtils.i(log);
    }

    @Override
    protected void reportLog(String log, String message) {
        AppLogUtils.i(log,message);
    }
}

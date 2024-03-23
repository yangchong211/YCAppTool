package com.yc.library;

import com.yc.appcommoninter.report.LoggerReporter;
import com.yc.toolutils.system.AppLogUtils;

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

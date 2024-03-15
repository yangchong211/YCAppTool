package com.yc.lifehelper.impl;

import com.yc.appcommoninter.report.ExceptionReporter;

public class ExceptionReporterImpl extends ExceptionReporter {
    @Override
    protected void reportCrash(Throwable throwable) {
        //壳工程中可以拿到APM，比如上传到bugly平台上
    }

    @Override
    protected void reportCrash(String tag, Throwable throwable) {

    }

    @Override
    protected void reportException(String content) {

    }
}

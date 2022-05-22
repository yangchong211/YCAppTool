package com.sankuai.erp.component.appinit.common;


public interface ILogger {
    boolean isDebug();

    boolean isIsMainProcess();

    void demo(String msg);

    void d(String msg);

    void e(String msg);
}
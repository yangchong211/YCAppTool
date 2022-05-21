package com.sankuai.erp.component.appinit.common;

/**
 * 作者:王浩
 * 创建时间:2018/11/2
 * 描述:
 */
public interface ILogger {
    boolean isDebug();

    boolean isIsMainProcess();

    void demo(String msg);

    void d(String msg);

    void e(String msg);
}
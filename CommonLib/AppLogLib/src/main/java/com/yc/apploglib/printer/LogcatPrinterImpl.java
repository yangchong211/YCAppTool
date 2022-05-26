package com.yc.apploglib.printer;

import com.yc.apploglib.printer.AbsPrinter;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 打印日志到控制台
 *     revise:
 * </pre>
 */
public class LogcatPrinterImpl extends AbsPrinter {

    public static final String PRINTER_NAME = "LogcatPrinter";

    @Override
    public String name() {
        return PRINTER_NAME;
    }

    @Override
    public void println(int level, String tag, String message, Throwable tr) {
        if (tr != null) {
            message += " Desc: " + android.util.Log.getStackTraceString(tr);
        }
        android.util.Log.println(level, tag, message);
    }
}

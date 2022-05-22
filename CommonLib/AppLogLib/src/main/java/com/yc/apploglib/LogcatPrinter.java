package com.yc.apploglib;

class LogcatPrinter extends Printer {

    static final String PRINTER_NAME = "LogcatPrinter";

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

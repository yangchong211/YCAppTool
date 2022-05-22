package com.yc.tracesdk;

public class TimeTraceFactory {
    static class LogcatPrinterHolder {
        static final InterPrinter sPrinter = new LogcatPrinter();
    }

    public static TimeTrace countByMilliseconds(String event, boolean startCount) {
        return new TimeTrace(
                event, LogcatPrinterHolder.sPrinter, TimerHolder.MILLIS_TIMER, startCount);
    }

    public static TimeTrace countByMilliseconds(String event) {
        return countByMilliseconds(event, true);
    }

    public static TimeTrace countByNanoseconds(String event, boolean startCount) {
        return new TimeTrace(
                event, LogcatPrinterHolder.sPrinter, TimerHolder.NANO_TIMER, startCount);
    }

    public static TimeTrace countByNanoseconds(String event) {
        return countByNanoseconds(event, true);
    }
}


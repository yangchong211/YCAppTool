package com.yc.tracesdk;

public class SLogTimeTraceFactory {
    static class SLogPrinterHolder {
        static final InterPrinter sPrinter = new SLogPrinter();
    }

    public static TimeTrace countByMilliseconds(String event, boolean startCount) {
        return new TimeTrace(
                event, SLogPrinterHolder.sPrinter, TimerHolder.MILLIS_TIMER, startCount);
    }

    public static TimeTrace countByMilliseconds(String event) {
        return countByMilliseconds(event, true);
    }

    public static TimeTrace countByNanoseconds(String event, boolean startCount) {
        return new TimeTrace(
                event, SLogPrinterHolder.sPrinter, TimerHolder.NANO_TIMER, startCount);
    }

    public static TimeTrace countByNanoseconds(String event) {
        return countByNanoseconds(event, true);
    }
}


package com.yc.tracesdk;

public class TimeTrace {
    private static final String TAG = "TimeTrace";

    private final String mEvent;
    private final InterPrinter mPrinter;
    private final InterTimer mTimer;

    private long mStartTraceTime;
    private long mLastTraceTime;
    private boolean mStarted;

    TimeTrace(String event, InterPrinter printer, InterTimer timer) {
        mEvent = event;
        mPrinter = printer;
        mTimer = timer;
    }

    TimeTrace(String event, InterPrinter printer, InterTimer timer, boolean startCount) {
        this(event, printer, timer);
        if (startCount) {
            start();
        }
    }


    /**
     * start count.
     */
    public void start() {
        if (!mPrinter.enable()) {
            return;
        }
        mStartTraceTime = mTimer.currentTime();
        mLastTraceTime = mStartTraceTime;
        mStarted = true;
    }

    /**
     * all count stopped & count all time cost.
     */
    public void end() {
        if (!mStarted) {
            return;
        }

        mPrinter.print(TAG, "[%s][END][%d %s]",
                mEvent, mTimer.currentTime() - mStartTraceTime, mTimer.unitName());
        mStarted = false;
        mStartTraceTime = 0;
        mLastTraceTime = 0;
    }

    /**
     * time cost from last step.
     */
    public void step(String format, Object... args) {
        if (!mStarted) {
            return;
        }

        String msg;
        if (args != null && args.length > 0) {
            msg = String.format(format, args);
        } else {
            msg = format;
        }

        long curTime = mTimer.currentTime();
        mPrinter.print(TAG, "[%s][STEP][%d %s] %s",
                mEvent, curTime - mLastTraceTime, mTimer.unitName(), msg);
        mLastTraceTime = curTime;
    }
}

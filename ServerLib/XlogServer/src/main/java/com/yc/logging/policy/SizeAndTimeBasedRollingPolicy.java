package com.yc.logging.policy;

import androidx.annotation.RestrictTo;

import com.yc.logging.config.LoggerContext;
import com.yc.logging.LoggerFactory;
import com.yc.logging.constant.Type;
import com.yc.logging.util.LoggerUtils;
import com.yc.logging.util.RollingCalendar;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class SizeAndTimeBasedRollingPolicy extends AbstractRollingPolicy {

    private final RollingCalendar mRollingCalendar;
    private LogFileRemover mLogFileRemover;

    private long mArtificialCurrentTime = -1;
    private long mNextCheck = 0L;

    private InvocationGate invocationGate = new DefaultInvocationGate();
    private int mCurrentPeriodsCounter;

    private String mDate;
    private String mBufferId;

    private String mLogType;
    private File mActiveDir;

    public SizeAndTimeBasedRollingPolicy(Type logType, String bufferId) {
        mLogType = logType.name;
        mBufferId = bufferId;
        mDateInCurrentPeriod.setTime(getCurrentTime());
        mDate = LoggerUtils.formatForFileName(mDateInCurrentPeriod);
        mRollingCalendar = new RollingCalendar();

        reset();
    }

    private void reset() {
        mActiveDir = getActiveDir();
        mCurrentPeriodsCounter = findCurrentSubCounter(mActiveDir, mLogType, mBufferId, mDate);
        mLogFileRemover = new LogFileRemover(mActiveDir);
    }

    @Override
    public File getActiveDir() {
        return LoggerContext.getDefault().getMainLogPathDir();
    }

    @Override
    public boolean isTriggeringEvent(File activeFile) {
        long time = getCurrentTime();

        //first check for roll-over based on time
        if (mNextCheck == 0) {
            computeNextCheck();
            return true;
        }

        if (time > mNextCheck) {
            mCurrentPeriodsCounter = 0;
            setDateInCurrentPeriod(time);
            computeNextCheck();
            return true;
        }

        // next check for roll-over based on size
        if (invocationGate.isTooSoon(time)) {
            return false;
        }

        if (!getActiveDir().getPath().equals(mActiveDir.getPath())){
            reset();
            return true;
        }

        if (activeFile.length() > LoggerFactory.getConfig().getMaxFileSize()) {
            mCurrentPeriodsCounter++;
            return true;
        }

        return false;
    }

    @Override
    public void rollover() {
        if (this.mLogFileRemover != null) {
            this.mLogFileRemover.clean(mDateInCurrentPeriod);
        }
    }

    @Override
    public String getActiveFile() {
        return new File(
                getActiveDir(),
                mLogType + "-" + mBufferId + "-" + mDate + "-" + mCurrentPeriodsCounter + ".log")
                .getAbsolutePath();
    }

    public long getCurrentTime() {
        // if time is forced return the time set by user
        return mArtificialCurrentTime >= 0 ? mArtificialCurrentTime : System.currentTimeMillis();
    }

    private void computeNextCheck() {
        mNextCheck = mRollingCalendar.getNextTriggeringMillis(mDateInCurrentPeriod);
    }

    public static int findCurrentSubCounter(File logDir, String logType, String bufferId, String date) {
        File[] files = LoggerUtils.collectLogFiles(logDir, logType, bufferId, date);
        if (files.length == 0) return 0;

        int max = 0;
        Pattern regex = LoggerUtils.getLogFileRegex(logType);
        for (File file : files) {
            Matcher m = regex.matcher(file.getName());
            if (m.matches()) {
                int counter = Integer.valueOf(m.group(4));
                if (max < counter) max = counter;
            }
        }
        return max;
    }
}

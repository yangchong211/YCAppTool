package com.yc.logging;

import androidx.annotation.RestrictTo;
import com.yc.logging.util.Debug;
import com.yc.logging.util.LoggerUtils;
import com.yc.logging.util.RollingCalendar;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;


@RestrictTo(RestrictTo.Scope.LIBRARY)
class LogFileRemover {

    static final long UNINITIALIZED = -1;

    // 14 days in case of hourly rollover aim for 64 days, except in case of hourly rollover
    static final int MAX_VALUE_FOR_INACTIVITY_PERIODS = 14 * 24;
    static final long INACTIVITY_TOLERANCE_IN_MILLIS = 64L * TimeUnit.DAYS.toMillis(1);

    private final RollingCalendar mRollingCalendar;

    private int mMaxHistory = LoggerFactory.getConfig().getFileMaxHistory();
    private int mPeriodOffsetForDeletionTarget;

    private long totalSizeCap = LoggerFactory.getConfig().getTotalFileSize();

    private long mLastHeartBeat = UNINITIALIZED;
    private File mLogDir;

    LogFileRemover(File logDir) {
        mLogDir = logDir;
        mRollingCalendar = new RollingCalendar();
        setMaxHistory(mMaxHistory);
    }

    void clean(final Date now) {
        final long nowInMillis = now.getTime();
        final int periodsElapsed = computeElapsedPeriodsSinceLastClean(nowInMillis);
        mLastHeartBeat = nowInMillis;
        for (int i = 0; i < periodsElapsed; i++) {
            int offset = mPeriodOffsetForDeletionTarget - i;
            cleanByPeriodOffset(now, offset);
        }
        capTotalSize(now);
    }

    private int computeElapsedPeriodsSinceLastClean(final long nowInMillis) {
        long periodsElapsed = 0;

        if (mLastHeartBeat == UNINITIALIZED) {
            periodsElapsed =
                    mRollingCalendar.periodsElapsed(nowInMillis, nowInMillis + INACTIVITY_TOLERANCE_IN_MILLIS);
            if (periodsElapsed > MAX_VALUE_FOR_INACTIVITY_PERIODS) {
                periodsElapsed = MAX_VALUE_FOR_INACTIVITY_PERIODS;
            }
        } else {
            periodsElapsed = mRollingCalendar.periodsElapsed(mLastHeartBeat, nowInMillis);
            if (periodsElapsed < 1) {
                periodsElapsed = 1;
            }
        }

        return (int) periodsElapsed;
    }

    private void cleanByPeriodOffset(Date now, int periodOffset) {
        Date date2delete = mRollingCalendar.getRelativeDate(now, periodOffset);
        File[] matchingFileArray =
                LoggerUtils.collectLogFiles(mLogDir, null, date2delete);
        if (matchingFileArray != null && matchingFileArray.length != 0) {
            for (File f : matchingFileArray) {
                Debug.d("rm file:" + f);
                f.delete();
            }
        }
    }

    void setMaxHistory(int maxHistory) {
        mMaxHistory = maxHistory;
        mPeriodOffsetForDeletionTarget = -maxHistory - 1;
    }


    void capTotalSize(Date now) {
        int totalSize = 0;
        Set<String> lastestFiles = new HashSet<>();
        for (int offset = 0; offset < mMaxHistory; offset++) {
            Date date = mRollingCalendar.getRelativeDate(now, -offset);
            File[] matchingFileArray = LoggerUtils.collectLogFiles(mLogDir, null, date);
            if (matchingFileArray != null && matchingFileArray.length > 0) {
                descendingSortByLastModified(matchingFileArray);
                for (File f : matchingFileArray) {
                    long size = f.length();
                    if (totalSize + size < totalSizeCap) {
                        lastestFiles.add(f.getName());
                    } else {
                        break;
                    }
                    totalSize += size;
                }
            }
        }
        File[] allLogFiles = LoggerUtils.collectLogFiles(mLogDir);
        if (allLogFiles != null && allLogFiles.length > lastestFiles.size()) {
            for (File f : allLogFiles) {
                if (!lastestFiles.contains(f.getName())) {
                    Debug.d("rm file:" + f);
                    f.delete();
                }
            }
        }
    }

    private void descendingSortByLastModified(File[] matchingFileArray) {
        if (matchingFileArray == null) {
            return;
        }

        for (int i = 0; i < matchingFileArray.length - 1; i++) {
            File min = matchingFileArray[i];
            int minindex = i;
            for (int j = i; j < matchingFileArray.length; j++) {
                if (matchingFileArray[j].lastModified() > min.lastModified()) {
                    min = matchingFileArray[j];
                    minindex = j;
                }
            }
            if (i != minindex) {
                matchingFileArray[minindex] = matchingFileArray[i];
                matchingFileArray[i] = min;
            }
        }
    }
}

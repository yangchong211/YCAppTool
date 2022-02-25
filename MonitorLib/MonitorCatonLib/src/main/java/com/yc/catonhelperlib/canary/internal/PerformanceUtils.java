
package com.yc.catonhelperlib.canary.internal;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.yc.catonhelperlib.canary.monitor.BlockCanaryInternals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

class PerformanceUtils {
    private static final String TAG = "PerformanceUtils";

    private static int sCoreNum = 0;
    private static long sTotalMemo = 0;

    private PerformanceUtils() {
        throw new InstantiationError("Must not instantiate this class");
    }

    /**
     * Get cpu core number
     *
     * @return int cpu core number
     */
    public static int getNumCores() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                return Pattern.matches("cpu[0-9]", pathname.getName());
            }
        }

        if (sCoreNum == 0) {
            try {
                // Get directory containing CPU info
                File dir = new File("/sys/devices/system/cpu/");
                // Filter to only list the devices we care about
                File[] files = dir.listFiles(new CpuFilter());
                // Return the number of cores (virtual CPU devices)
                sCoreNum = files.length;
            } catch (Exception e) {
                Log.e(TAG, "getNumCores exception", e);
                sCoreNum = 1;
            }
        }
        return sCoreNum;
    }

    public static long getFreeMemory() {
        ActivityManager am = (ActivityManager) BlockCanaryInternals.getContext().provideContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem / 1024;
    }

    public static long getTotalMemory() {
        if (sTotalMemo == 0) {
            String str1 = "/proc/meminfo";
            String str2;
            String[] arrayOfString;
            long initial_memory = -1;
            FileReader localFileReader = null;
            try {
                localFileReader = new FileReader(str1);
                BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
                str2 = localBufferedReader.readLine();

                if (str2 != null) {
                    arrayOfString = str2.split("\\s+");
                    initial_memory = Integer.valueOf(arrayOfString[1]);
                }
                localBufferedReader.close();

            } catch (IOException e) {
                Log.e(TAG, "getTotalMemory exception = ", e);
            } finally {
                if (localFileReader != null) {
                    try {
                        localFileReader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "close localFileReader exception = ", e);
                    }
                }
            }
            sTotalMemo = initial_memory;
        }
        return sTotalMemo;
    }
}
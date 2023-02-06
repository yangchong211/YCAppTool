package com.yc.m3u8.utils;

/**
 * 网速工具
 * Created by HDL on 2017/8/14.
 */

public class NetSpeedUtils {
    private static NetSpeedUtils mNetSpeedUtils;

    private NetSpeedUtils() {
    }

    public static NetSpeedUtils getInstance() {
        synchronized (NetSpeedUtils.class) {
            if (mNetSpeedUtils == null) {
                mNetSpeedUtils = new NetSpeedUtils();
            }
        }
        return mNetSpeedUtils;
    }

    //字节大小，K,M,G
    public static final long KB = 1024;
    public static final long MB = KB * 1024;
    public static final long GB = MB * 1024;

    /**
     * 文件字节大小显示成M,G和K
     *
     * @param size
     * @return
     */
    public String displayFileSize(long size) {
        if (size >= GB) {
            return String.format("%.1f GB", (float) size / GB);
        } else if (size >= MB) {
            float value = (float) size / MB;
            return String.format(value > 100 ? "%.0f MB" : "%.1f MB", value);
        } else if (size >= KB) {
            float value = (float) size / KB;
            return String.format(value > 100 ? "%.0f KB" : "%.1f KB", value);
        } else {
            return String.format("%d B", size);
        }
    }
}

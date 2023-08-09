package com.yc.monitorflow;

import android.net.TrafficStats;

public class TrafficStatsHelper {

    /**
     * 接收的总数据流量
     *
     * @return 流量
     */
    public static long getAllRxBytes() {
        return TrafficStats.getTotalRxBytes();
    }

    /**
     * 发送的总数据流量
     *
     * @return 流量
     */
    public static long getAllTxBytes() {
        return TrafficStats.getTotalTxBytes();
    }

    /**
     * 移动网络接受的总流量
     *
     * @return 流量
     */
    public static long getAllRxBytesMobile() {
        return TrafficStats.getMobileRxBytes();
    }

    /**
     * 移动网络发送的总流量
     *
     * @return 流量
     */
    public static long getAllTxBytesMobile() {
        return TrafficStats.getMobileTxBytes();
    }

    /**
     * Wi-Fi网络接受的总流量
     *
     * @return 流量
     */
    public static long getAllRxBytesWifi() {
        return TrafficStats.getTotalRxBytes() - TrafficStats.getMobileRxBytes();
    }

    /**
     * Wi-Fi网络发送的总流量
     *
     * @return 流量
     */
    public static long getAllTxBytesWifi() {
        return TrafficStats.getTotalTxBytes() - TrafficStats.getMobileTxBytes();
    }

    /**
     * 指定uid接收的流量
     *
     * @return 流量
     */
    public static long getPackageRxBytes(int uid) {
        return TrafficStats.getUidRxBytes(uid);
    }

    /**
     * 指定uid发送的流量
     *
     * @return 流量
     */
    public static long getPackageTxBytes(int uid) {
        return TrafficStats.getUidTxBytes(uid);
    }

}

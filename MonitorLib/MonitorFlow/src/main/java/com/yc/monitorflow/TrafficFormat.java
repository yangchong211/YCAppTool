package com.yc.monitorflow;

import java.text.DecimalFormat;

public final class TrafficFormat {

    //定义TB的计算常量
    private static final double TB = 1024f * 1024f * 1024f * 1024f;
    //定义GB的计算常量
    private static final double GB = 1024f * 1024f * 1024f;
    //定义MB的计算常量
    private static final double MB = 1024f * 1024f;
    //定义KB的计算常量
    private static final double KB = 1024f;

    /**
     * 格式化数据
     *
     * @param data 流量数据
     * @return 返回格式化后的流量
     */
    public static String formatByte(long data) {
        DecimalFormat format = new DecimalFormat("####.##");
        if (data < KB) {
            return data + "B";
        } else if (data < MB) {
            return format.format(data / KB) + "KB";
        } else if (data < GB) {
            return format.format(data / MB) + "MB";
        } else if (data < TB) {
            return format.format(data / GB) + "GB";
        } else {
            return "超出统计范围";
        }
    }

}

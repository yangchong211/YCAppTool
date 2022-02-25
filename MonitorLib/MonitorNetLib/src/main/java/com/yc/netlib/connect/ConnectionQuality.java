package com.yc.netlib.connect;

/**
 * 不同连接质量的一般枚举
 */
public enum ConnectionQuality {
    /**
     * 带宽小于150kbps
     */
    POOR,
    /**
     * 带宽在150到550kbps之间。
     */
    MODERATE,
    /**
     * 带宽在550到2000kbps之间。
     */
    GOOD,
    /**
     * 带宽超过2000kbps
     */
    EXCELLENT,
    /**
     * 未知带宽的占位符。这是初始值，如果不能准确地找到带宽，它将保持在这个值。
     */
    UNKNOWN
}

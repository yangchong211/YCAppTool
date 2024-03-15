package com.yc.toolutils.memory;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/5/6
 *     desc  : 手机RAM内存信息，物理内存信息
 *     revise:
 * </pre>
 */
public class RamMemoryInfo {

    /**
     * 可用RAM【系统可用内存】
     * 获取内存剩余大小（以字节为单位）
     */
    public long availMem;
    /**
     * 手机总RAM【总内存】
     * 获取内存总大小（以字节为单位）
     */
    public long totalMem;
    /**
     * 内存占用满的阀值，超过即认为低内存运行状态，可能会Kill process。【低内存阈值，即低内存的临界线】
     * 获取内存阈值（以字节为单位）
     */
    public long lowMemThreshold;
    /**
     * 是否低内存状态运行
     */
    public boolean isLowMemory;

}

package com.yc.toolmemorylib;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/5/6
 *     desc  : 应用实际占用内存（共享按比例分配）
 *     revise:
 * </pre>
 */
public final class PssInfo {
    public int totalPss;
    public int dalvikPss;
    public int nativePss;
    public int otherPss;
}

package com.yc.toolutils.memory;

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
    /**
     * 进程的总内存大小
     * 整体内存，native+dalvik+共享
     */
    public long totalPss;
    /**
     * java内存 OOM原因
     * 重点关注对象
     */
    public long dalvikPss;
    /**
     * native内存
     */
    public long nativePss;
    /**
     * 其他内存
     */
    public long otherPss;
}

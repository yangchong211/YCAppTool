package com.yc.appencryptlib;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/7/10
 *     desc  : hash工具类
 *     revise:
 * </pre>
 */
public final class HashEncryptUtils {

    /** Cache the hash code for the string */
    private static int hash;

    /**
     * String字符串hash算法
     * @param string            字符串
     * @return
     */
    public static int hashCode(String string) {
        int h = hash;
        final int len = string.length();
        if (h == 0 && len > 0) {
            for (int i = 0; i < len; i++) {
                h = 31 * h + string.charAt(i);
            }
            hash = h;
        }
        return h;
    }



}

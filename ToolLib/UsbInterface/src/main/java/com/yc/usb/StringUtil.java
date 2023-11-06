package com.yc.usb;

import android.text.TextUtils;

import java.security.MessageDigest;

/**
 * MD5工具
 */
public class StringUtil {

    /**
     * 获取字符串的MD5值，32位
     * @param code 源字符串
     * @return 32位md5
     */
    public static String getMD5(String code) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(code.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 找到findByStr在str中出现的次数
     * @param str 源字符串
     * @param findByStr 被查询的字符串
     * @return 返回findByStr在str中出现的次数
     */
    public static int strCount(String str,String findByStr){
        if(TextUtils.isEmpty(str) | TextUtils.isEmpty(findByStr)){
            return 0;
        }
        //原来的字符串的长度
        int origialLength = str.length();
        //将字符串中的指定字符去掉
        str = str.replace(findByStr, "");
        //去掉指定字符的字符串长度
        int newLength = str.length();
        //需要查找的字符串的长度
        int findStrLength = findByStr.length();
        //返回指定字符出现的次数
        return (origialLength - newLength)/findStrLength;
    }
}

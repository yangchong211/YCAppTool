package com.yc.appencryptlib;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/7/10
 *     desc  : base64工具类
 *     revise:
 * </pre>
 */
public final class Base64Utils {

    /**
     * 字符Base64加密
     * @param str
     * @return
     */
    public static String encodeToString(String str){
        try {
            byte[] bytes = str.getBytes("UTF-8");
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 字符Base64解密
     * @param str
     * @return
     */
    public static String decodeToString(String str){
        try {
            byte[] bytes = str.getBytes("UTF-8");
            byte[] decode = Base64.decode(bytes, Base64.DEFAULT);
            return new String(decode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}

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
     * 无论是编码还是解码都会有一个参数Flags，Android提供了以下几种
     * DEFAULT 这个参数是默认，使用默认的方法来加密
     * NO_PADDING 这个参数是略去加密字符串最后的”=”
     * NO_WRAP 这个参数意思是略去所有的换行符（设置后CRLF就没用了）
     * CRLF 这个参数看起来比较眼熟，它就是Win风格的换行符，意思就是使用CR LF这一对作为一行的结尾而不是Unix风格的LF
     * URL_SAFE 这个参数意思是加密时不使用对URL和文件名有特殊意义的字符来作为加密字符，具体就是以-和_取代+和/
     */


    /**
     * 字符Base64加密
     * @param str           字符串
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
     * @param str           字符串
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



    public static byte[] base64Encode(final byte[] input) {
        return Base64.encode(input, Base64.DEFAULT);
    }

    public static byte[] base64Decode(final byte[] input) {
        return Base64.decode(input, Base64.DEFAULT);
    }


}

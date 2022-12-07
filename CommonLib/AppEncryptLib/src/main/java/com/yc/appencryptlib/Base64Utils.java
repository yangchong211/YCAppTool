package com.yc.appencryptlib;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

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

    private Base64Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断字符串是否BASE64编码：
     * 1.字符串只可能包含A-Z，a-z，0-9，+，/，=字符
     * 2.字符串长度是4的倍数
     * 3.=只会出现在字符串最后，可能没有或者一个等号或者两个等号
     */
    public static boolean isBase64(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    /**
     * 是否是base64图片
     * @param imgUrl            image图片字符串
     * @return
     */
    public static boolean isBase64Img(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl) && (
                imgUrl.startsWith("data:image/png;base64,")
                        || imgUrl.startsWith("data:image/*;base64,")
                        || imgUrl.startsWith("data:image/jpg;base64,")
                        || imgUrl.startsWith("data:image/jpeg;base64,"))) {
            return true;
        }
        return false;
    }

    /**
     * 将数据进行Base64编码，并转为可展示的ASCII字符串.
     * 编码失败不会抛出异常，编码失败会返回NULL.
     */
    public static String encodeToString(byte[] data) {
        try {
            byte[] encode = Base64.encode(data, Base64.DEFAULT);
            // 使用GBK及UTF-8字符集都是兼容ASCII字符集的
            //noinspection CharsetObjectCanBeUsed
            return new String(encode, "UTF-8").trim();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将数据进行Base64编码，并转为可展示的ASCII字符串.
     * 编码失败不会抛出异常，编码失败会返回NULL.
     */
    public static String encodeToStringWrap(byte[] data) {
        try {
            byte[] encode = Base64.encode(data, Base64.NO_WRAP);
            // 使用GBK及UTF-8字符集都是兼容ASCII字符集的
            //noinspection CharsetObjectCanBeUsed
            return new String(encode, "UTF-8").trim();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

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
     * 将BASE64编码的字符串解码.
     * 解码失败不会抛出异常，解码失败会返回NULL.
     */
    public static byte[] decodeFromString(String data) {
        try {
            byte[] bytes = data.trim().getBytes();
            return Base64.decode(bytes,Base64.DEFAULT);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将BASE64编码的字符串解码.
     * 解码失败不会抛出异常，解码失败会返回NULL.
     */
    public static byte[] decodeFromStringWrap(String data) {
        try {
            byte[] bytes = data.trim().getBytes();
            return Base64.decode(bytes,Base64.NO_WRAP);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
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

    /**
     * 字符Base64解密
     * @param str           字符串
     * @return
     */
    public static String decodeToStringWrap(String str){
        try {
            byte[] bytes = str.getBytes("UTF-8");
            byte[] decode = Base64.decode(bytes, Base64.NO_WRAP);
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

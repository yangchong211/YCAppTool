package com.yc.zxingserver.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/6/16
 *     desc  : 诸葛书md加密+加盐工具类
 *     revise:
 * </pre>
 */
public final class ZXingMd5Utils {


    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * MD5 加密
     *
     * @param data 明文字符串
     * @return 16 进制密文
     */
    public static String encryptMD5ToString(final String data) {
        return encryptMD5ToString(data.getBytes());
    }

    /**
     * MD5 加密
     *
     * @param data 明文字符串
     * @param salt 盐
     * @return 16 进制加盐密文
     */
    public static String encryptMD5ToString(final String data, final String salt) {
        return bytes2HexString(encryptMD5((data + salt).getBytes()));
    }

    /**
     * MD5 加密
     *
     * @param data 明文字节数组
     * @return 16 进制密文
     */
    public static String encryptMD5ToString(final byte[] data) {
        return bytes2HexString(encryptMD5(data));
    }

    /**
     * MD5 加密
     *
     * @param data 明文字节数组
     * @param salt 盐字节数组
     * @return 16 进制加盐密文
     */
    public static String encryptMD5ToString(final byte[] data, final byte[] salt) {
        if (data == null || salt == null) return null;
        byte[] dataSalt = new byte[data.length + salt.length];
        System.arraycopy(data, 0, dataSalt, 0, data.length);
        System.arraycopy(salt, 0, dataSalt, data.length, salt.length);
        return bytes2HexString(encryptMD5(dataSalt));
    }

    /**
     * MD5 加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    public static byte[] encryptMD5(final byte[] data) {
        return hashTemplate(data, "MD5");
    }


    /**
     * hash 加密模板
     *
     * @param data      数据
     * @param algorithm 加密算法
     * @return 密文字节数组
     */
    private static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }


}

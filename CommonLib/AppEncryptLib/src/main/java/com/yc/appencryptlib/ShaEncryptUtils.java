package com.yc.appencryptlib;

/**
 * <pre>
 *     author: yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2015/08/02
 *     desc  : 加密解密相关的工具类
 * </pre>
 */
public final class ShaEncryptUtils {

    /**
     * SHA包含5个算法，分别是SHA-1、SHA-224、SHA-256、SHA-384和SHA-512，后四者并称为SHA-2。
     *
     * SHA384和SHA512的区别
     * SHA系列算法的摘要长度分别为： SHA384为48字节（384位）、SHA512为64字节（512位）
     * 由于它产生的数据摘要的长度更长，因此更难以发生碰撞，因此也更为安全，它是未来数据摘要算法的发展方向。
     * 由于SHA系列算法的数据摘要长度较长，因此其运算速度与MD5相比，也相对较慢。
     *
     * SHA384和SHA512的区别就是摘要长度不同，它们都是安全散列算法，并且都是不可逆的。
     */
    private ShaEncryptUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 哈希加密相关
    ///////////////////////////////////////////////////////////////////////////

    /**
     * SHA1 加密
     *
     * @param data 明文字符串
     * @return 16 进制密文
     */
    public static String encryptSHA1ToString(final String data) {
        return encryptSHA1ToString(data.getBytes());
    }

    /**
     * SHA1 加密
     *
     * @param data 明文字节数组
     * @return 16 进制密文
     */
    public static String encryptSHA1ToString(final byte[] data) {
        return BaseEncryptUtils.bytes2HexString(encryptSHA1(data));
    }

    /**
     * SHA1 加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    public static byte[] encryptSHA1(final byte[] data) {
        return BaseEncryptUtils.hashTemplate(data, "SHA1");
    }

    /**
     * SHA224 加密
     *
     * @param data 明文字符串
     * @return 16 进制密文
     */
    public static String encryptSHA224ToString(final String data) {
        return encryptSHA224ToString(data.getBytes());
    }

    /**
     * SHA224 加密
     *
     * @param data 明文字节数组
     * @return 16 进制密文
     */
    public static String encryptSHA224ToString(final byte[] data) {
        return BaseEncryptUtils.bytes2HexString(encryptSHA224(data));
    }

    /**
     * SHA224 加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    public static byte[] encryptSHA224(final byte[] data) {
        return BaseEncryptUtils.hashTemplate(data, "SHA224");
    }

    /**
     * SHA256 加密
     *
     * @param data 明文字符串
     * @return 16 进制密文
     */
    public static String encryptSHA256ToString(final String data) {
        return encryptSHA256ToString(data.getBytes());
    }

    /**
     * SHA256 加密
     *
     * @param data 明文字节数组
     * @return 16 进制密文
     */
    public static String encryptSHA256ToString(final byte[] data) {
        return BaseEncryptUtils.bytes2HexString(encryptSHA256(data));
    }

    /**
     * SHA256 加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    public static byte[] encryptSHA256(final byte[] data) {
        return BaseEncryptUtils.hashTemplate(data, "SHA256");
    }

    /**
     * SHA384 加密
     * SHA-384是一种安全散列算法，最大计算明文长度为2^128bit，属于分组算法，分组长度为1024bit，产生的信息摘要长度为384bit。
     * SHA-384算法属于密码杂凑算法，原则上不能通过密文推出明文。
     *
     * @param data 明文字符串
     * @return 16 进制密文
     */
    public static String encryptSHA384ToString(final String data) {
        return encryptSHA384ToString(data.getBytes());
    }

    /**
     * SHA384 加密
     * SHA-384是一种安全散列算法，最大计算明文长度为2^128bit，属于分组算法，分组长度为1024bit，产生的信息摘要长度为384bit。
     * SHA-384算法属于密码杂凑算法，原则上不能通过密文推出明文。
     *
     * @param data 明文字节数组
     * @return 16 进制密文
     */
    public static String encryptSHA384ToString(final byte[] data) {
        return BaseEncryptUtils.bytes2HexString(encryptSHA384(data));
    }

    /**
     * SHA384 加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    public static byte[] encryptSHA384(final byte[] data) {
        return BaseEncryptUtils.hashTemplate(data, "SHA384");
    }

    /**
     * SHA512 加密
     * SHA512是一种安全散列算法，有时候也被称作 SHA-2。
     * 对于称为sh512的哈希算法来说，这是一个易于理解的演练，包括一些基本和简单的数学知识以及一些图表。
     * 它是SHA-2 家族的一员，其中包括SHA256，也用于比特币区块链的哈希算法。
     *
     * @param data 明文字符串
     * @return 16 进制密文
     */
    public static String encryptSHA512ToString(final String data) {
        return encryptSHA512ToString(data.getBytes());
    }

    /**
     * SHA512 加密
     *
     * @param data 明文字节数组
     * @return 16 进制密文
     */
    public static String encryptSHA512ToString(final byte[] data) {
        return BaseEncryptUtils.bytes2HexString(encryptSHA512(data));
    }

    /**
     * SHA512 加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    public static byte[] encryptSHA512(final byte[] data) {
        return BaseEncryptUtils.hashTemplate(data, "SHA512");
    }



}

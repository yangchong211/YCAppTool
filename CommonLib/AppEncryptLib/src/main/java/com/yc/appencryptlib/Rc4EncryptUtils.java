package com.yc.appencryptlib;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/7/10
 *     desc  : rc4加解密
 *     revise:
 * </pre>
 */
public final class Rc4EncryptUtils {

    /**
     * RC4 对称密码算法的工作方式有四种：
     * 1.电子密码本(ECB, electronic codebook)方式
     * 2.密码分组链接(CBC, cipherblock chaining)方式
     * 3.密文反馈(CFB, cipher-feedback)方式
     * 4.输出反馈(OFB, output-feedback)方式
     *
     * RC4算法采用的是输出反馈工作方式，所以可以用一个短的密钥产生一个相对较长的密钥序列。
     * 输出反馈工作方式
     * 最大的优点是消息如果发生错误(这里指的是消息的某一位发生了改变，而不是消息的某一位丢失)，错误不会传递到产生的密钥序列上；
     * 缺点是对插入攻击很敏感，并且对同步的要求比较高。
     *
     */

    /**
     * RC4加密，加密失败将返回空串。
     */
    public static String encryptString(String data, String secretKey) {
        try {
            return encryptToBase64(data.getBytes(),secretKey);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * RC4加密字节数据。
     */
    public static byte[] encryptToByte(byte[] data, String secretKey) {
        try {
            return convert(data, secretKey);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RC4加密base64编码数据，加密失败将返回空串。
     */
    public static String encryptToBase64(byte[] data, String secretKey) {
        try {
            byte[] convert = convert(data, secretKey);
            return Base64Utils.encodeToStringWrap(convert);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * RC4解密，解密失败将返回NULL。
     */
    public static String decryptString(String base64, String secretKey) {
        try {
            byte[] decryptFromBase64 = decryptFromBase64(base64, secretKey);
            return new String(decryptFromBase64);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decryptByte(byte[] data, String secretKey) {
        try {
            return convert(data, secretKey);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RC4解密，解密失败将返回NULL。
     */
    public static byte[] decryptFromBase64(String base64, String secretKey) {
        try {
            //将BASE64编码的字符串解码
            byte[] decodeFromString = Base64Utils.decodeFromStringWrap(base64);
            return convert(decodeFromString, secretKey);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RC4加解密，会自动识别传入的是密文还是明文。加解密失败将抛异常。
     */
    private static byte[] convert(byte[] data, String secretKey) throws IllegalArgumentException {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data cannot be empty");
        }
        if (secretKey == null || secretKey.trim().length() == 0) {
            throw new IllegalArgumentException("Key cannot be empty");
        }
        //初始化密钥
        byte[] bkey = secretKey.getBytes();
        if (bkey.length == 0 || bkey.length > 256) {
            throw new IllegalArgumentException("Key length must 1-256");
        }
        byte[] key = new byte[256];
        for (int i = 0; i < 256; i++) {
            key[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;

        for (int i = 0; i < 256; i++) {
            index2 = ((bkey[index1] & 0xff) + (key[i] & 0xff) + index2) & 0xff;
            byte tmp = key[i];
            key[i] = key[index2];
            key[index2] = tmp;
            index1 = (index1 + 1) % bkey.length;
        }
        //开始加解密
        int x = 0;
        int y = 0;
        int xorIndex;
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (data[i] ^ key[xorIndex]);
        }
        return result;
    }

}

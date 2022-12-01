package com.yc.appencryptlib;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/7/10
 *     desc  : aes工具类
 *     revise: 对称加密
 * </pre>
 */
public final class AesEncryptUtils {
    /**
     * AES 转变
     * <p>法算法名称/加密模式/填充方式</p>
     * <p>加密模式有：电子密码本模式 ECB、加密块链模式 CBC、加密反馈模式 CFB、输出反馈模式 OFB</p>
     * <p>填充方式有：NoPadding、ZerosPadding、PKCS5Padding</p>
     */
    private static final String ALGORITHM = "AES";
    private final static String HEX = "0123456789ABCDEF";
    public static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final String CHAR_ENCODING = "UTF-8";

    private AesEncryptUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * AES加密字符串
     *
     * @param data 待加密字符串
     * @param key  加密密码，长度不能够小于8位
     * @return 加密后内容
     */
    public static String encrypt(String data,String key){
        byte[] rawKey;
        try {
            rawKey = getRawKey(key.getBytes());
            byte[] result = encrypt(rawKey, data.getBytes());
            return toHex(result);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * AES解密字符串
     * @param data 待解密字符串
     * @param key  解密密码，长度不能够小于8位
     * @return 解密后内容，也就是明文
     */
    public static String decrypt(String data, String key){
        byte[] rawKey;
        try {
            rawKey = getRawKey(key.getBytes());
            byte[] enc = toByte(data);
            byte[] result = decrypt(rawKey, enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        // SHA1PRNG 强随机种子算法
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        // 256位或128位或192位
        kgen.init(256, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] key, byte[] src) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src);
        return encrypted;
    }

    private static byte[] decrypt(byte[] key, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    private static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    ///////////////////////////////////////////////////////////////////////////
    // AES 加密相关
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 加密
     *
     * @param data 需要加密的内容
     * @param key  加密密码
     * @return
     */
    public static byte[] encryptAes(byte[] data, byte[] key) {
        if (key.length != 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);// 创建密码器
            IvParameterSpec iv = new IvParameterSpec(key);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, seckey, iv);// 初始化
            byte[] result = cipher.doFinal(data);
            return result; // 加密
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @param key  解密密钥
     * @return
     */
    public static byte[] decryptAes(byte[] data, byte[] key) {
        if (key.length != 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, ALGORITHM);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(key);
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, seckey, iv);
            // 解密
            byte[] result = cipher.doFinal(data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("decrypt fail!", e);
        }
    }

    public static String encryptToBase64(String data, String key) {
        try {
            byte[] valueByte = encryptAes(data.getBytes(CHAR_ENCODING), key.getBytes(CHAR_ENCODING));
            byte[] base64Encode = Base64Utils.base64Encode(valueByte);
            return new String(base64Encode);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encrypt fail!", e);
        }

    }

    public static String decryptFromBase64(String data, String key) {
        try {
            byte[] originalData = Base64Utils.base64Decode(data.getBytes());
            byte[] valueByte = decryptAes(originalData, key.getBytes(CHAR_ENCODING));
            return new String(valueByte, CHAR_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("decrypt fail!", e);
        }
    }

    public static String encryptWithKeyBase64(String data, String key) {
        try {
            byte[] keyBytes = key.getBytes();
            byte[] base64Decode = Base64Utils.base64Decode(keyBytes);
            byte[] dataBytes = data.getBytes(CHAR_ENCODING);
            byte[] valueByte = encryptAes(dataBytes, base64Decode);
            byte[] base64Encode = Base64Utils.base64Encode(valueByte);
            return new String(base64Encode);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    public static String decryptWithKeyBase64(String data, String key) {
        try {
            byte[] originalData = Base64Utils.base64Encode(data.getBytes());
            byte[] base64Decode = Base64Utils.base64Decode(key.getBytes());
            byte[] valueByte = decryptAes(originalData, base64Decode);
            return new String(valueByte, CHAR_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("decrypt fail!", e);
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // AES 加密相关
    ///////////////////////////////////////////////////////////////////////////

    /**
     * AES 加密后转为 Base64 编码
     *
     * @param data     明文
     * @param password 8 字节秘钥
     * @return Base64 密文
     */
    public static String encryptAES(final byte[] data, final byte[] password) {
        byte[] ivParameterBytes = new byte[0];
        try {
            ivParameterBytes = HEX.getBytes(CHAR_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] bytes = encryptAES2Base64(data, password, AES_ALGORITHM, ivParameterBytes);
        byte[] bytesEncode = Base64Utils.base64Encode(bytes);
        return new String(bytesEncode);
    }

    /**
     * DES解密字符串
     *
     * @param data     Base64 编码密文
     * @param key 16、24、32 字节秘钥
     * @return 解密后内容，也就是明文
     */
    public static String decryptAES(final byte[] data, final byte[] key) {
        byte[] ivParameterBytes = new byte[0];
        try {
            ivParameterBytes = HEX.getBytes(CHAR_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] bytes = decryptBase64AES(data, key, AES_ALGORITHM, ivParameterBytes);
        return new String(bytes);
    }


    /**
     * AES 加密后转为 Base64 编码
     *
     * @param data           明文
     * @param key            16、24、32 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return Base64 密文
     */
    public static byte[] encryptAES2Base64(final byte[] data,
                                           final byte[] key,
                                           final String transformation,
                                           final byte[] iv) {
        return Base64Utils.base64Encode(encryptAES(data, key, transformation, iv));
    }

    /**
     * AES 加密后转为 16 进制
     *
     * @param data           明文
     * @param key            16、24、32 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 16 进制密文
     */
    public static String encryptAES2HexString(final byte[] data,
                                              final byte[] key,
                                              final String transformation,
                                              final byte[] iv) {
        return BaseEncryptUtils.bytes2HexString(encryptAES(data, key, transformation, iv));
    }

    /**
     * AES 加密
     *
     * @param data           明文
     * @param key            16、24、32 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 密文
     */
    public static byte[] encryptAES(final byte[] data,
                                    final byte[] key,
                                    final String transformation,
                                    final byte[] iv) {
        return desTemplate(data, key, ALGORITHM, transformation, iv, true);
    }

    /**
     * AES 解密 Base64 编码密文
     *
     * @param data           Base64 编码密文
     * @param key            16、24、32 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decryptBase64AES(final byte[] data,
                                          final byte[] key,
                                          final String transformation,
                                          final byte[] iv) {
        byte[] base64Decode = Base64Utils.base64Decode(data);
        return decryptAES(base64Decode, key, transformation, iv);
    }

    /**
     * AES 解密 16 进制密文
     *
     * @param data           16 进制密文
     * @param key            16、24、32 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decryptHexStringAES(final String data,
                                             final byte[] key,
                                             final String transformation,
                                             final byte[] iv) {
        return decryptAES(BaseEncryptUtils.hexString2Bytes(data), key, transformation, iv);
    }

    /**
     * AES 解密
     *
     * @param data           密文
     * @param key            16、24、32 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decryptAES(final byte[] data,
                                    final byte[] key,
                                    final String transformation,
                                    final byte[] iv) {
        return desTemplate(data, key, ALGORITHM, transformation, iv, false);
    }

    /**
     * 加密模板
     *
     * @param data           数据
     * @param key            秘钥
     * @param algorithm      加密算法
     * @param transformation 转变
     * @param isEncrypt      {@code true}: 加密 {@code false}: 解密
     * @return 密文或者明文，适用于 DES，3DES，AES
     */
    private static byte[] desTemplate(final byte[] data,
                                      final byte[] key,
                                      final String algorithm,
                                      final String transformation,
                                      final byte[] iv,
                                      final boolean isEncrypt) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return null;
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            Cipher cipher = Cipher.getInstance(transformation);
            if (iv == null || iv.length == 0) {
                cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec);
            } else {
                AlgorithmParameterSpec params = new IvParameterSpec(iv);
                cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, params);
            }
            return cipher.doFinal(data);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

}

package com.yc.appencryptlib;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class DesEncryptUtils {


    ///////////////////////////////////////////////////////////////////////////
    // DES 加密相关
    ///////////////////////////////////////////////////////////////////////////

    /**
     * DES 转变
     * <p>法算法名称/加密模式/填充方式</p>
     * <p>加密模式有：电子密码本模式 ECB、加密块链模式 CBC、加密反馈模式 CFB、输出反馈模式 OFB</p>
     * <p>填充方式有：NoPadding、ZerosPadding、PKCS5Padding</p>
     */
    private static final String DES_Algorithm = "DES";

    /**
     * DES 加密后转为 Base64 编码
     *
     * @param data           明文
     * @param key            8 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return Base64 密文
     */
    public static byte[] encryptDES2Base64(final byte[] data,
                                           final byte[] key,
                                           final String transformation,
                                           final byte[] iv) {
        return BaseEncryptUtils.base64Encode(encryptDES(data, key, transformation, iv));
    }

    /**
     * DES 加密后转为 16 进制
     *
     * @param data           明文
     * @param key            8 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 16 进制密文
     */
    public static String encryptDES2HexString(final byte[] data,
                                              final byte[] key,
                                              final String transformation,
                                              final byte[] iv) {
        return BaseEncryptUtils.bytes2HexString(encryptDES(data, key, transformation, iv));
    }

    /**
     * DES 加密
     *
     * @param data           明文
     * @param key            8 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 密文
     */
    public static byte[] encryptDES(final byte[] data,
                                    final byte[] key,
                                    final String transformation,
                                    final byte[] iv) {
        return desTemplate(data, key, DES_Algorithm, transformation, iv, true);
    }

    /**
     * DES 解密 Base64 编码密文
     *
     * @param data           Base64 编码密文
     * @param key            8 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decryptBase64DES(final byte[] data,
                                          final byte[] key,
                                          final String transformation,
                                          final byte[] iv) {
        return decryptDES(BaseEncryptUtils.base64Decode(data), key, transformation, iv);
    }

    /**
     * DES 解密 16 进制密文
     *
     * @param data           16 进制密文
     * @param key            8 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decryptHexStringDES(final String data,
                                             final byte[] key,
                                             final String transformation,
                                             final byte[] iv) {
        return decryptDES(BaseEncryptUtils.hexString2Bytes(data), key, transformation, iv);
    }

    /**
     * DES 解密
     *
     * @param data           密文
     * @param key            8 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decryptDES(final byte[] data,
                                    final byte[] key,
                                    final String transformation,
                                    final byte[] iv) {
        return desTemplate(data, key, DES_Algorithm, transformation, iv, false);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 3DES 加密相关
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 3DES 转变
     * <p>法算法名称/加密模式/填充方式</p>
     * <p>加密模式有：电子密码本模式 ECB、加密块链模式 CBC、加密反馈模式 CFB、输出反馈模式 OFB</p>
     * <p>填充方式有：NoPadding、ZerosPadding、PKCS5Padding</p>
     */
    private static final String TripleDES_Algorithm = "DESede";


    /**
     * 3DES 加密后转为 Base64 编码
     *
     * @param data           明文
     * @param key            24 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return Base64 密文
     */
    public static byte[] encrypt3DES2Base64(final byte[] data,
                                            final byte[] key,
                                            final String transformation,
                                            final byte[] iv) {
        return BaseEncryptUtils.base64Encode(encrypt3DES(data, key, transformation, iv));
    }

    /**
     * 3DES 加密后转为 16 进制
     *
     * @param data           明文
     * @param key            24 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 16 进制密文
     */
    public static String encrypt3DES2HexString(final byte[] data,
                                               final byte[] key,
                                               final String transformation,
                                               final byte[] iv) {
        return BaseEncryptUtils.bytes2HexString(encrypt3DES(data, key, transformation, iv));
    }

    /**
     * 3DES 加密
     *
     * @param data           明文
     * @param key            24 字节密钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 密文
     */
    public static byte[] encrypt3DES(final byte[] data,
                                     final byte[] key,
                                     final String transformation,
                                     final byte[] iv) {
        return desTemplate(data, key, TripleDES_Algorithm, transformation, iv, true);
    }

    /**
     * 3DES 解密 Base64 编码密文
     *
     * @param data           Base64 编码密文
     * @param key            24 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decryptBase64_3DES(final byte[] data,
                                            final byte[] key,
                                            final String transformation,
                                            final byte[] iv) {
        return decrypt3DES(BaseEncryptUtils.base64Decode(data), key, transformation, iv);
    }

    /**
     * 3DES 解密 16 进制密文
     *
     * @param data           16 进制密文
     * @param key            24 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decryptHexString3DES(final String data,
                                              final byte[] key,
                                              final String transformation,
                                              final byte[] iv) {
        return decrypt3DES(BaseEncryptUtils.hexString2Bytes(data), key, transformation, iv);
    }

    /**
     * 3DES 解密
     *
     * @param data           密文
     * @param key            24 字节密钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decrypt3DES(final byte[] data,
                                     final byte[] key,
                                     final String transformation,
                                     final byte[] iv) {
        return desTemplate(data, key, TripleDES_Algorithm, transformation, iv, false);
    }

    /**
     * DES 加密模板
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

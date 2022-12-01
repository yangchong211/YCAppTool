package com.yc.appencryptlib;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/7/10
 *     desc  : des工具类
 *     revise: 对称加密
 * </pre>
 */
public final class DesEncryptUtils {


    private DesEncryptUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 偏移变量，固定占8位字节
     */
    private final static String IV_PARAMETER = "88886666";
    /**
     * 密钥算法
     * DES 转变
     * <p>法算法名称/加密模式/填充方式</p>
     * <p>加密模式有：电子密码本模式 ECB、加密块链模式 CBC、加密反馈模式 CFB、输出反馈模式 OFB</p>
     * <p>填充方式有：NoPadding、ZerosPadding、PKCS5Padding</p>
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";

    /**
     * 生成key
     *
     * @param password 加密密码，长度不能够小于8位
     * @return key
     * @throws Exception 异常
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }


    /**
     * DES加密字符串
     *
     * @param password 加密密码，长度不能够小于8位
     * @param data     待加密字符串
     * @return 加密后内容
     */
    public static String encrypt(String data, String password) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null) {
            return null;
        }
        try {
            Key secretKey = generateKey(password);
            //创建密码器
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            byte[] iv_parameterBytes = IV_PARAMETER.getBytes(CHARSET);
            //实例化IvParameterSpec对象，使用指定的初始化向量
            IvParameterSpec iv = new IvParameterSpec(iv_parameterBytes);
            //用密钥初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] dataBytes = data.getBytes(CHARSET);
            //执行加密操作
            byte[] bytes = cipher.doFinal(dataBytes);
            //JDK1.8及以上可直接使用Base64，JDK1.7及以下可以使用BASE64Encoder
            //Android平台可以使用android.util.Base64
            byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
            return new String(encode);
            //return new String(cipher.doFinal(data.getBytes(CHARSET)), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES解密字符串
     *
     * @param password 解密密码，长度不能够小于8位
     * @param data     待解密字符串
     * @return 解密后内容，也就是明文
     */
    public static String decrypt(String data, String password) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null) {
            return null;
        }
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            byte[] iv_parameterBytes = IV_PARAMETER.getBytes(CHARSET);
            IvParameterSpec iv = new IvParameterSpec(iv_parameterBytes);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] decode = Base64.decode(data.getBytes(CHARSET), Base64.DEFAULT);
            return new String(cipher.doFinal(decode), CHARSET);
            //return new String(cipher.doFinal(data.getBytes(CHARSET)), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES加密文件
     *
     * @param srcFile  待加密的文件
     * @param destFile 加密后存放的文件路径
     * @return 加密后的文件路径
     */
    public static String encryptFile(String password, String srcFile, String destFile) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(password), iv);
            InputStream is = new FileInputStream(srcFile);
            OutputStream out = new FileOutputStream(destFile);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }
            cis.close();
            is.close();
            out.close();
            return destFile;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * DES解密文件
     *
     * @param srcFile  已加密的文件
     * @param destFile 解密后存放的文件路径
     * @return 解密后的文件路径
     */
    public static String decryptFile(String password, String srcFile, String destFile) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        try {
            File file = new File(destFile);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, generateKey(password), iv);
            InputStream is = new FileInputStream(srcFile);
            OutputStream out = new FileOutputStream(destFile);
            CipherOutputStream cos = new CipherOutputStream(out, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = is.read(buffer)) >= 0) {
                cos.write(buffer, 0, r);
            }
            cos.close();
            is.close();
            out.close();
            return destFile;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    ///////////////////////////////////////////////////////////////////////////
    // DES 加密相关
    ///////////////////////////////////////////////////////////////////////////

    /**
     * DES 加密后转为 Base64 编码
     *
     * @param data     明文
     * @param password 8 字节秘钥
     * @return Base64 密文
     */
    public static String encrypt(final byte[] data, final String password) {
        return encrypt(data, password.getBytes());
    }

    /**
     * DES 加密后转为 Base64 编码
     *
     * @param data     明文
     * @param password 8 字节秘钥
     * @return Base64 密文
     */
    public static String encrypt(final byte[] data, final byte[] password) {
        byte[] ivParameterBytes = new byte[0];
        try {
            ivParameterBytes = IV_PARAMETER.getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] bytes = encryptDES(data, password, CIPHER_ALGORITHM, ivParameterBytes);
        byte[] bytesEncode = Base64Utils.base64Encode(bytes);
        return new String(bytesEncode);
    }

    /**
     * DES 加密后转为 Base64 编码
     *
     * @param data     明文
     * @param password 8 字节秘钥
     * @param iv       初始化向量
     * @return Base64 密文
     */
    public static String encrypt(final byte[] data, final byte[] password, final byte[] iv) {
        byte[] bytesEncode = encryptDES2Base64(data, password, CIPHER_ALGORITHM, iv);
        return new String(bytesEncode);
    }

    /**
     * DES 加密后转为 Base64 编码
     *
     * @param data           明文
     * @param password       8 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return Base64 密文
     */
    private static byte[] encryptDES2Base64(final byte[] data,
                                            final byte[] password,
                                            final String transformation,
                                            final byte[] iv) {
        byte[] bytes = encryptDES(data, password, transformation, iv);
        byte[] base64Encode = Base64Utils.base64Encode(bytes);
        return base64Encode;
    }

    /**
     * DES解密字符串
     *
     * @param data     Base64 编码密文
     * @param password 8 字节秘钥
     * @return 解密后内容，也就是明文
     */
    public static String decrypt(final byte[] data, final byte[] password) {
        byte[] ivParameterBytes = new byte[0];
        try {
            ivParameterBytes = IV_PARAMETER.getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] bytes = decryptBase64DES(data, password, CIPHER_ALGORITHM, ivParameterBytes);
        return new String(bytes);
    }

    /**
     * DES解密字符串
     *
     * @param data     Base64 编码密文
     * @param password 8 字节秘钥
     * @return 解密后内容，也就是明文
     */
    public static String decrypt(final byte[] data, final byte[] password, final byte[] iv) {
        byte[] bytes = decryptBase64DES(data, password, CIPHER_ALGORITHM, iv);
        return new String(bytes);
    }


    /**
     * DES 解密 Base64 编码密文
     *
     * @param data           Base64 编码密文
     * @param password       8 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decryptBase64DES(final byte[] data,
                                          final byte[] password,
                                          final String transformation,
                                          final byte[] iv) {
        byte[] bytes = Base64Utils.base64Decode(data);
        byte[] decryptDES = decryptDES(bytes, password, transformation, iv);
        return decryptDES;
    }

    /**
     * DES 加密后转为 16 进制
     *
     * @param data           明文
     * @param password       8 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 16 进制密文
     */
    public static String encryptDES2HexString(final byte[] data,
                                              final byte[] password,
                                              final String transformation,
                                              final byte[] iv) {
        byte[] bytes = encryptDES(data, password, transformation, iv);
        return BaseEncryptUtils.bytes2HexString(bytes);
    }

    /**
     * DES 解密 16 进制密文
     *
     * @param data           16 进制密文
     * @param password       8 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decryptHexStringDES(final String data,
                                             final byte[] password,
                                             final String transformation,
                                             final byte[] iv) {
        return decryptDES(BaseEncryptUtils.hexString2Bytes(data), password, transformation, iv);
    }

    /**
     * DES 加密
     *
     * @param data           明文
     * @param password       8 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 密文
     */
    private static byte[] encryptDES(final byte[] data,
                                     final byte[] password,
                                     final String transformation,
                                     final byte[] iv) {
        return desTemplate(data, password, ALGORITHM, transformation, iv, true);
    }

    /**
     * DES 解密
     *
     * @param data           密文
     * @param password       8 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    private static byte[] decryptDES(final byte[] data,
                                     final byte[] password,
                                     final String transformation,
                                     final byte[] iv) {
        return desTemplate(data, password, ALGORITHM, transformation, iv, false);
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
     * @param password       24 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return Base64 密文
     */
    public static byte[] encrypt3DES2Base64(final byte[] data,
                                            final byte[] password,
                                            final String transformation,
                                            final byte[] iv) {
        return Base64Utils.base64Encode(encrypt3DES(data, password, transformation, iv));
    }

    /**
     * 3DES 加密后转为 16 进制
     *
     * @param data           明文
     * @param password       24 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 16 进制密文
     */
    public static String encrypt3DES2HexString(final byte[] data,
                                               final byte[] password,
                                               final String transformation,
                                               final byte[] iv) {
        return BaseEncryptUtils.bytes2HexString(encrypt3DES(data, password, transformation, iv));
    }

    /**
     * 3DES 加密
     *
     * @param data           明文
     * @param password       24 字节密钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 密文
     */
    public static byte[] encrypt3DES(final byte[] data,
                                     final byte[] password,
                                     final String transformation,
                                     final byte[] iv) {
        return desTemplate(data, password, TripleDES_Algorithm, transformation, iv, true);
    }

    /**
     * 3DES 解密 Base64 编码密文
     *
     * @param data           Base64 编码密文
     * @param password       24 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decryptBase64_3DES(final byte[] data,
                                            final byte[] password,
                                            final String transformation,
                                            final byte[] iv) {
        return decrypt3DES(Base64Utils.base64Decode(data), password, transformation, iv);
    }

    /**
     * 3DES 解密 16 进制密文
     *
     * @param data           16 进制密文
     * @param password       24 字节秘钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decryptHexString3DES(final String data,
                                              final byte[] password,
                                              final String transformation,
                                              final byte[] iv) {
        return decrypt3DES(BaseEncryptUtils.hexString2Bytes(data), password, transformation, iv);
    }

    /**
     * 3DES 解密
     *
     * @param data           密文
     * @param password       24 字节密钥
     * @param transformation 转变
     * @param iv             初始化向量
     * @return 明文
     */
    public static byte[] decrypt3DES(final byte[] data,
                                     final byte[] password,
                                     final String transformation,
                                     final byte[] iv) {
        return desTemplate(data, password, TripleDES_Algorithm, transformation, iv, false);
    }

    /**
     * DES 加密模板
     *
     * @param data           数据
     * @param password       秘钥
     * @param algorithm      加密算法
     * @param transformation 转变
     * @param isEncrypt      {@code true}: 加密 {@code false}: 解密
     * @return 密文或者明文，适用于 DES，3DES，AES
     */
    private static byte[] desTemplate(final byte[] data,
                                      final byte[] password,
                                      final String algorithm,
                                      final String transformation,
                                      final byte[] iv,
                                      final boolean isEncrypt) {
        if (data == null || data.length == 0 || password == null || password.length == 0) {
            return null;
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(password, algorithm);
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

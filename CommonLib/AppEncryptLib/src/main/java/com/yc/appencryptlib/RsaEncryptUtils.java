package com.yc.appencryptlib;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/7/10
 *     desc  : rsa工具类
 *     revise: 非对称加密
 * </pre>
 */
public final class RsaEncryptUtils {

    /**
     * 非对称加密密钥算法
     */
    public static final String RSA = "RSA";
    /**
     * 加密填充方式
     */
    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";



    /**
     * 随机生成RSA密钥对
     *
     * @param keyLength         密钥长度，范围：512～2048
     *                          一般1024
     * @return
     */
    public static KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*-------------------------------------------------------------------------------------------------*/


    /**
     * 用公钥对数据进行加密
     * @param data                      原文
     * @param publicKey                 密钥
     * @return                          byte[] 解密数据
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance(ECB_PKCS1_PADDING);
        cp.init(Cipher.ENCRYPT_MODE, keyPublic);
        return cp.doFinal(data);
    }


    /**
     * 使用私钥进行解密
     * @param encrypted                 待解密数据
     * @param privateKey                密钥
     * @return                          byte[] 解密数据
     * @throws Exception                异常
     */
    public static byte[] decryptByPrivateKey(byte[] encrypted, byte[] privateKey) throws Exception {
        // 得到私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 解密数据
        Cipher cp = Cipher.getInstance(ECB_PKCS1_PADDING);
        cp.init(Cipher.DECRYPT_MODE, keyPrivate);
        byte[] arr = cp.doFinal(encrypted);
        return arr;
    }


    /**
     * 私钥加密
     *
     * @param data                      待加密数据
     * @param privateKey                密钥
     * @return                          byte[] 解密数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) throws Exception {
        // 得到私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keyPrivate);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data                      待解密数据
     * @param publicKey                 密钥
     * @return                          byte[] 解密数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, keyPublic);
        return cipher.doFinal(data);
    }



    private static final String PUBLIC_KEY_BEGIN = "-----BEGIN RSA PUBLIC KEY-----";
    private static final String PUBLIC_KEY_END = "-----END RSA PUBLIC KEY-----";
    private static final String PRIVATE_KEY_BEGIN = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String PRIVATE_KEY_END = "-----END RSA PRIVATE KEY-----";
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    /**
     * 使用RSA公钥加密对称加密算法所使用的明文密码。
     * 说明：RSA只支持小数据加密，对于大批量数据加密，不建议对数据分段加密，强烈建议使用以下方案：
     * 第一步：客户端选择合适的对称加密算法（如AES、RC4、XXTEA），该算法须服务端也支持；
     * 第二步：客户端随机生成一个对称加密算法所用的密码，使用该密码加密数据，再使用RSA公钥加密该密码；
     * 第三步：服务端使用RSA私钥解密出对称加密算法所用的密码，再使用该密码及同样的对称加密算法解密数据。
     * 推荐查阅这篇文章帮助理解：https://www.cnblogs.com/JeffreySun/archive/2010/06/24/1627247.html
     */
    public static byte[] encrypt(byte[] plainSecretKey, RSAPublicKey publicKey) throws Exception {
        if (plainSecretKey == null || plainSecretKey.length == 0) {
            throw new IllegalArgumentException("plain SecretKey is empty");
        }
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plainSecretKey);
    }

    public static String encryptToBase64(byte[] plainSecretKey, RSAPublicKey publicKey) {
        try {
            return Base64Utils.encodeToString(encrypt(plainSecretKey, publicKey));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 使用RSA私钥解密对称加密算法所使用的密文密码
     */
    public static byte[] decrypt(byte[] encryptedSecretKey, RSAPrivateKey privateKey) throws Exception {
        if (encryptedSecretKey == null || encryptedSecretKey.length == 0) {
            throw new IllegalArgumentException("encrypted SecretKey is empty");
        }
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedSecretKey);

    }

    public static byte[] decryptFromBase64(String base64, RSAPrivateKey privateKey) {
        try {
            return decrypt(Base64Utils.decodeFromString(base64), privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用私钥对数据签名。
     * 支持的算法参见 https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Signature
     */
    public static byte[] signature(byte[] data, RSAPrivateKey privateKey, String algorithm) throws Exception {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data is empty");
        }
        if (privateKey == null) {
            throw new IllegalArgumentException("private key is null");
        }
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static String signatureMD5(byte[] data, RSAPrivateKey privateKey) {
        try {
            byte[] md5withRSAS = signature(data, privateKey, "MD5withRSA");
            return Md5EncryptUtils.encryptMD5ToString(md5withRSAS);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 使用公钥验证数据签名
     */
    public static boolean verify(byte[] data, RSAPublicKey publicKey, byte[] sign, String algorithm) throws Exception {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data is empty");
        }
        if (publicKey == null) {
            throw new IllegalArgumentException("public key is null");
        }
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(sign);
    }

    public static boolean verifyMD5(byte[] data, RSAPublicKey publicKey, String sign) {
        try {
            return verify(data, publicKey, sign.getBytes(), "MD5withRSA");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifySHA1(byte[] data, RSAPublicKey publicKey, String sign) {
        try {
            return verify(data, publicKey, sign.getBytes(), "SHA1withRSA");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 随机生成RSA密钥对
     */
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            //密钥长度范围：512～2048，一般1024
            generator.initialize(1024);
            return generator.genKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据二进制BASE64编码的X509证书生成X509证书
     */
    public static X509Certificate generateCertificate(byte[] base64) {
        if (base64 == null || base64.length == 0) {
            Log.d("RSA: ","base64 is empty");
            return null;
        }
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(base64);
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            X509Certificate x509Certificate = (X509Certificate) factory.generateCertificate(stream);
            return x509Certificate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据字符串BASE64编码的X509证书生成X509证书
     */
    public static X509Certificate generateCertificate(String base64) {
        base64 = ignoreIdentifier(base64);
        byte[] data = Base64Utils.base64Decode(base64.getBytes());
        if (data == null) {
            return null;
        }
        return generateCertificate(data);
    }

    /**
     * 根据二进制BASE64编码的公钥生成RSA公钥
     */
    public static RSAPublicKey generatePublicKey(byte[] base64) {
        if (base64 == null || base64.length == 0) {
            Log.d("RSA: ","base64 is empty");
            return null;
        }
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //Only RSAPublicKeySpec and X509EncodedKeySpec supported for RSA public keys
            return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(base64));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据字符串BASE64编码的公钥生成RSA公钥
     */
    public static RSAPublicKey generatePublicKey(String base64) {
        base64 = ignoreIdentifier(base64);
        byte[] data = Base64Utils.base64Decode(base64.getBytes());
        if (data == null) {
            return null;
        }
        return generatePublicKey(data);
    }

    /**
     * 根据二进制BASE64编码的私钥生成RSA私钥
     */
    public static RSAPrivateKey generatePrivateKey(byte[] base64) {
        if (base64 == null || base64.length == 0) {
            Log.d("RSA: ","base64 is empty");
            return null;
        }
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            try {
                return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(base64));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
                return (RSAPrivateKey) keyFactory.generatePrivate(new X509EncodedKeySpec(base64));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据字符串BASE64编码的私钥生成RSA私钥
     */
    public static RSAPrivateKey generatePrivateKey(String base64) {
        base64 = ignoreIdentifier(base64);
        byte[] data = Base64Utils.base64Decode(base64.getBytes());
        if (data == null) {
            return null;
        }
        return generatePrivateKey(data);
    }

    private static String ignoreIdentifier(String str) {
        StringBuilder sb = new StringBuilder();
        String[] lines = str.split("\n");
        for (String line : lines) {
            //忽略证书及秘钥串首尾的标识符
            if (line.charAt(0) != '-') {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    /**
     * 根据模数及公钥指数生成RSA公钥
     */
    public static RSAPublicKey generatePublicKey(String modulus, String publicExponent) {
        try {
            BigInteger bigIntModulus = new BigInteger(modulus);
            BigInteger bigIntExponent = new BigInteger(publicExponent);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntExponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据模数及私钥指数生成RSA私钥
     */
    public static RSAPrivateKey generatePrivateKey(String modulus, String privateExponent) {
        try {
            BigInteger bigIntModulus = new BigInteger(modulus);
            BigInteger bigIntExponent = new BigInteger(privateExponent);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntExponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将RSA公钥编码为BASE64字符串
     */
    public static String encodePublicKeyToString(RSAPublicKey publicKey, boolean excludeIdentifier) {
        String encode = Base64Utils.encodeToString(publicKey.getEncoded());
        if (excludeIdentifier) {
            return encode;
        }
        return PUBLIC_KEY_BEGIN + "\n" + encode + "\n" + PUBLIC_KEY_END;
    }

    /**
     * 将RSA私钥编码为BASE64字符串
     */
    public static String encodePrivateKeyToString(RSAPrivateKey privateKey, boolean excludeIdentifier) {
        String encode = Base64Utils.encodeToString(privateKey.getEncoded());
        if (excludeIdentifier) {
            return encode;
        }
        return PRIVATE_KEY_BEGIN + "\n" + encode + "\n" + PRIVATE_KEY_END;
    }


    public static void printPublicKeyInfo(RSAPublicKey publicKey) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("RSA Public Key Info:").append("\n");
            sb.append("Format=").append(publicKey.getFormat()).append("\n");
            sb.append("Algorithm=").append(publicKey.getAlgorithm()).append("\n");
            sb.append("Modulus.length=").append(publicKey.getModulus().bitLength()).append("\n");
            sb.append("Modulus=").append(publicKey.getModulus().toString()).append("\n");
            sb.append("PublicExponent.length=").append(publicKey.getPublicExponent().bitLength()).append("\n");
            sb.append("PublicExponent=").append(publicKey.getPublicExponent().toString()).append("\n");
        } catch (Exception e) {
            sb.append(e);
        }
        Log.d("RSA: ",sb.toString());
    }

    public static void printPrivateKeyInfo(RSAPrivateKey privateKey) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("RSA Private Key Info:").append("\n");
            sb.append("Format=").append(privateKey.getFormat()).append("\n");
            sb.append("Algorithm=").append(privateKey.getAlgorithm()).append("\n");
            sb.append("Modulus.length=").append(privateKey.getModulus().bitLength()).append("\n");
            sb.append("Modulus=").append(privateKey.getModulus().toString()).append("\n");
            sb.append("PrivateExponent.length=").append(privateKey.getPrivateExponent().bitLength()).append("\n");
            sb.append("PrivateExponent=").append(privateKey.getPrivateExponent().toString()).append("\n");
        } catch (Exception e) {
            sb.append(e);
        }
        Log.d("RSA: ",sb.toString());
    }
}

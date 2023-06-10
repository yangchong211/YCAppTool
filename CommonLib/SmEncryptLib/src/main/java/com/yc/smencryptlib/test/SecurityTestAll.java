package com.yc.smencryptlib.test;

import cn.xjfme.encrypt.utils.Util;
import cn.xjfme.encrypt.utils.sm2.SM2EncDecUtils;
import cn.xjfme.encrypt.utils.sm2.SM2KeyVO;
import cn.xjfme.encrypt.utils.sm2.SM2SignVO;
import cn.xjfme.encrypt.utils.sm2.SM2SignVerUtils;
import cn.xjfme.encrypt.utils.sm4.SM4Utils;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.util.UUID;

public class SecurityTestAll {
    //SM2公钥编码格式
    //HardPubKey:3059301306072A8648CE3D020106082A811CCF5501822D03420004+X+Y
    //SoftPubKey:04+X+Y
    public static final String SM2PubHardKeyHead = "3059301306072A8648CE3D020106082A811CCF5501822D034200";
    //SM2加密 密文区别:软加密多了04
    //SM2加密机签名编码格式
    //HardSign:R+S
    //public static final String SM2PubHardKeyHead="3059301306072A8648CE3D020106082A811CCF5501822D034200";
    //private final String SM4_CBC_IV="";
    //private final String SM2="";


    public static void main(String[] args) throws Exception {
        System.out.println("--产生SM2秘钥--:");
        SM2KeyVO sm2KeyVO = generateSM2Key();
        System.out.println("公钥:" + sm2KeyVO.getPubHexInSoft());
        System.out.println("私钥:" + sm2KeyVO.getPriHexInSoft());
        //数据加密
        System.out.println("--测试加密开始--");
        String src = "I Love You";
        System.out.println("原文UTF-8转hex:" + Util.byteToHex(src.getBytes()));
        String SM2Enc = SM2Enc(sm2KeyVO.getPubHexInSoft(), src);
        System.out.println("加密:");
        System.out.println("密文:" + SM2Enc);
        String SM2Dec = SM2Dec(sm2KeyVO.getPriHexInSoft(), SM2Enc);
        System.out.println("解密:" + SM2Dec);
        System.out.println("--测试加密结束--");

        System.out.println("--测试SM2签名--");
        System.out.println("原文hex:" + Util.byteToHex(src.getBytes()));
        String s5 = Util.byteToHex(src.getBytes());

        System.out.println("签名测试开始:");
        SM2SignVO sign = genSM2Signature(sm2KeyVO.getPriHexInSoft(), s5);
        System.out.println("软加密签名结果:" + sign.getSm2_signForSoft());
        System.out.println("加密机签名结果:" + sign.getSm2_signForHard());
        //System.out.println("转签名测试:"+SM2SignHardToSoft(sign.getSm2_signForHard()));
        System.out.println("验签1,软件加密方式:");
        boolean b = verifySM2Signature(sm2KeyVO.getPubHexInSoft(), s5, sign.getSm2_signForSoft());
        System.out.println("软件加密方式验签结果:" + b);
        System.out.println("验签2,硬件加密方式:");
        String sm2_signForHard = sign.getSm2_signForHard();
        System.out.println("签名R:"+sign.sign_r);
        System.out.println("签名S:"+sign.sign_s);
        //System.out.println("硬:"+sm2_signForHard);
        b = verifySM2Signature(sm2KeyVO.getPubHexInSoft(), s5, SM2SignHardToSoft(sign.getSm2_signForHard()));
        System.out.println("硬件加密方式验签结果:" + b);
        if (!b) {
            throw new RuntimeException();
        }
        System.out.println("--签名测试结束--");

        System.out.println("--SM3摘要测试--");
        String s = generateSM3HASH(src);
        System.out.println("hash:"+s);
        System.out.println("--SM3摘要结束--");

        System.out.println("--生成SM4秘钥--");
        String sm4Key = generateSM4Key();
        System.out.println("sm4Key:"+sm4Key);
        System.out.println("--生成SM4结束--");
        System.out.println("--SM4的CBC加密--");
        String s1 = SM4EncForCBC(sm4Key, src);
        System.out.println("密文:"+s1);
        System.out.println("CBC解密");
        String s2 = SM4DecForCBC(sm4Key, s1);
        System.out.println("解密结果:"+s2);
        System.out.println("--ECB加密--");
        String s3 = SM4EncForECB(sm4Key, src);
        System.out.println("ECB密文:"+s3);
        System.out.println("ECB解密");
        String s4 = SM4DecForECB(sm4Key, s3);
        System.out.println("ECB解密结果:"+s4);
    }

    //SM2公钥soft和Hard转换
    public static String SM2PubKeySoftToHard(String softKey) {
        return SM2PubHardKeyHead + softKey;
    }

    //SM2公钥Hard和soft转换
    public static String SM2PubKeyHardToSoft(String hardKey) {
        return hardKey.replaceFirst(SM2PubHardKeyHead, "");
    }

    //产生非对称秘钥
    public static SM2KeyVO generateSM2Key() throws IOException {
        SM2KeyVO sm2KeyVO = SM2EncDecUtils.generateKeyPair();
        return sm2KeyVO;
    }

    //公钥加密
    public static String SM2Enc(String pubKey, String src) throws IOException {
        String encrypt = SM2EncDecUtils.encrypt(Util.hexStringToBytes(pubKey), src.getBytes());
        //删除04
        encrypt=encrypt.substring(2,encrypt.length());
        return encrypt;
    }

    //私钥解密
    public static String SM2Dec(String priKey, String encryptedData) throws IOException {
        //填充04
        encryptedData="04"+encryptedData;
        byte[] decrypt = SM2EncDecUtils.decrypt(Util.hexStringToBytes(priKey), Util.hexStringToBytes(encryptedData));
        return new String(decrypt);
    }

    //私钥签名,参数二:原串必须是hex!!!!因为是直接用于计算签名的,可能是SM3串,也可能是普通串转Hex
    public static SM2SignVO genSM2Signature(String priKey, String sourceData) throws Exception {
        SM2SignVO sign = SM2SignVerUtils.Sign2SM2(Util.hexToByte(priKey), Util.hexToByte(sourceData));
        return sign;
    }

    //公钥验签,参数二:原串必须是hex!!!!因为是直接用于计算签名的,可能是SM3串,也可能是普通串转Hex
    public static boolean verifySM2Signature(String pubKey, String sourceData, String hardSign) {
        SM2SignVO verify = SM2SignVerUtils.VerifySignSM2(Util.hexStringToBytes(pubKey), Util.hexToByte(sourceData), Util.hexToByte(hardSign));
        return verify.isVerify();
    }

    //SM2签名Hard转soft
    public static String SM2SignHardToSoft(String hardSign) {
        byte[] bytes = Util.hexToByte(hardSign);
        byte[] r = new byte[bytes.length / 2];
        byte[] s = new byte[bytes.length / 2];
        System.arraycopy(bytes, 0, r, 0, bytes.length / 2);
        System.arraycopy(bytes, bytes.length / 2, s, 0, bytes.length / 2);
        ASN1Integer d_r = new ASN1Integer(Util.byteConvertInteger(r));
        ASN1Integer d_s = new ASN1Integer(Util.byteConvertInteger(s));
        ASN1EncodableVector v2 = new ASN1EncodableVector();
        v2.add(d_r);
        v2.add(d_s);
        DERSequence sign = new DERSequence(v2);

        String result = null;
        try {
            result = Util.byteToHex(sign.getEncoded());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //SM2加密机转软加密编码格式
        //return SM2SignHardKeyHead+hardSign.substring(0, hardSign.length()/2)+SM2SignHardKeyMid+hardSign.substring(hardSign.length()/2);
        return result;
    }

    //摘要计算
    public static String generateSM3HASH(String src) {
        byte[] md = new byte[32];
        byte[] msg1 = src.getBytes();
        //System.out.println(Util.byteToHex(msg1));
        SM3Digest sm3 = new SM3Digest();
        sm3.update(msg1, 0, msg1.length);
        sm3.doFinal(md, 0);
        String s = new String(Hex.encode(md));
        return s.toUpperCase();
    }

    //产生对称秘钥
    public static String generateSM4Key() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    //对称秘钥加密(CBC)
    public static String SM4EncForCBC(String key,String text) {
        SM4Utils sm4 = new SM4Utils();
        sm4.secretKey = key;
        sm4.hexString = true;
        sm4.iv = "31313131313131313131313131313131";
        String cipherText = sm4.encryptData_CBC(text);
        return cipherText;
    }

    //对称秘钥解密(CBC)
    public static String SM4DecForCBC(String key,String text) {
        SM4Utils sm4 = new SM4Utils();
        sm4.secretKey = key;
        sm4.hexString = true;
        sm4.iv = "31313131313131313131313131313131";
        String plainText = sm4.decryptData_CBC(text);
        return plainText;
    }
    //对称秘钥加密(ECB)
    public static String SM4EncForECB(String key,String text) {
        SM4Utils sm4 = new SM4Utils();
        sm4.secretKey = key;
        sm4.hexString = true;
        String cipherText = sm4.encryptData_ECB(text);
        return cipherText;
    }
    //对称秘钥解密(ECB)
    public static String SM4DecForECB(String key,String text) {
        SM4Utils sm4 = new SM4Utils();
        sm4.secretKey = key;
        sm4.hexString = true;
        String plainText = sm4.decryptData_ECB(text);
        return plainText;
    }

}

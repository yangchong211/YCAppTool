package com.yc.smencryptlib.test;



import com.yc.smencryptlib.Util;
import com.yc.smencryptlib.sm2.SM2EncDecUtils;
import com.yc.smencryptlib.sm2.SM2SignVO;
import com.yc.smencryptlib.sm2.SM2SignVerUtils;

import java.io.IOException;

public class SecurityTest {

    public void sm2sign() throws Exception {
        String src = "0653F3748DFD938FE83935800FF3F526B85C30C2331DD56FCB1794AA99F2A416";
        String text = "这是一段明文";
        byte[] sourceData = text.getBytes();
        //String publicKey ="FA05C51AD1162133DFDF862ECA5E4A481B52FB37FF83E53D45FD18BBD6F32668A92C4692EEB305684E3B9D4ACE767F91D5D108234A9F07936020A92210BA9447";
        //String privatekey = "5EB4DF17021CC719B678D970C620690A11B29C8357D71FA4FF9BF7FB6D89767A";
        String publicKey = "042780f0963a428a7b030ac1c14a90b967bf365f5394ebf1f0ca1598d4d9bece4fdfa05ba043817fef68bef497088e3992362ce55b1858444fa5a3e00c5042b207";
        String privatekey = "73e83d33d95274eeeb23f01834d02fe920b4afece377410435698dfdf1d84203";

        SM2SignVO sign = SM2SignVerUtils.Sign2SM2(Util.hexStringToBytes(privatekey), Util.hexToByte(src));
        System.out.println("R:"+sign.sign_r);
        System.out.println("S:"+sign.sign_s);
        //验签硬加密的串
        String signYJ = "54720652E5EE53D14F338A03EDAC10E7F93D877EC2168F9287810807D02D2409F3EEE542638AD0B204BC3C8F93EDBCFBE87DEEFB07C0B36F34508AB49B6F90EF";
        SM2SignVO verify = SM2SignVerUtils.VerifySignSM2(Util.hexStringToBytes(publicKey), Util.hexToByte(src), Util.hexToByte(SecurityTestAll.SM2SignHardToSoft(signYJ)));
        System.err.println("验签结果" + verify.isVerify());
    }

    @Test
    public void sm2sign2() throws Exception {
        String src = "32472f598b61ea4ff28f54b00f12ca0a8c1596e2867c5cce4afcb19ee93c2cde";
        String text = "这是一段明文";
        byte[] sourceData = text.getBytes();
        //String publicKey ="FA05C51AD1162133DFDF862ECA5E4A481B52FB37FF83E53D45FD18BBD6F32668A92C4692EEB305684E3B9D4ACE767F91D5D108234A9F07936020A92210BA9447";
        //String privatekey = "5EB4DF17021CC719B678D970C620690A11B29C8357D71FA4FF9BF7FB6D89767A";
        String publicKey = "0485B52403AEB742F952EFF7200BDBA0A399F0971FEB0EAA0CBE00A1A5EE922A34A24BD9CD2EA740B84290838A862E432BD9BBC0CD0659FD6D172CD1871CD76068";
        String privatekey = "62329467E71E70960C7A479C03CA7FC0A2BE92E000240C4F4080F0B2437C536D";

        SM2SignVO sign = SM2SignVerUtils.Sign2SM2(Util.hexStringToBytes(privatekey), Util.hexToByte(src));
        System.out.println("R:"+sign.sign_r);
        System.out.println("S:"+sign.sign_s);
        //验签硬加密的串
        String signYJ = "233fabe6f81002fbee8c69d9561114d99e0640ecf27d63561d850d77ac76ee5f5d0530bd6eca60e960784f9ad883b77dcfa3c8b274918034faf509faeee2e5ea";
        SM2SignVO verify = SM2SignVerUtils.VerifySignSM2(Util.hexStringToBytes(publicKey), Util.hexToByte(src), Util.hexToByte(SecurityTestAll.SM2SignHardToSoft(signYJ)));
        System.err.println("验签结果" + verify.isVerify());
    }

    @Test
    public void sm2enc() throws IOException {
        String plainText = "ILoveYou11";
        //SM3测试
        //生成密钥对
        //generateKeyPair();
        byte[] sourceData = plainText.getBytes();

        //下面的秘钥可以使用generateKeyPair()生成的秘钥内容
        // 国密规范正式私钥
        //String prik = "3690655E33D5EA3D9A4AE1A1ADD766FDEA045CDEAA43A9206FB8C430CEFE0D94";
        // 国密规范正式公钥
        //String pubk = "04F6E0C3345AE42B51E06BF50B98834988D54EBC7460FE135A48171BC0629EAE205EEDE253A530608178A98F1E19BB737302813BA39ED3FA3C51639D7A20C7391A";

        String prik = "4cf170068e9c47ebdb521fb9fc62c4a55a5773fb9da33b0acf8129e28d09d205";
        String pubk = "04aabda53043e8dcb86d42f690b61a4db869821dadf9f851ec3c5c43d0c8f95a6677fdba984afc3bb010a8436b1d17cefc2011a34e01e9e801124d29ffa928d803";
        String publicKey = "04BB34D657EE7E8490E66EF577E6B3CEA28B739511E787FB4F71B7F38F241D87F18A5A93DF74E90FF94F4EB907F271A36B295B851F971DA5418F4915E2C1A23D6E";
        String privatekey = "0B1CE43098BC21B8E82B5C065EDB534CB86532B1900A49D49F3C53762D2997FA";
        prik = privatekey;
        pubk = publicKey;
        System.out.println("加密: ");
        String cipherText = SM2EncDecUtils.encrypt(Util.hexToByte(pubk), sourceData);
        //cipherText = "0452ba81cf5119c9f29c81c2be9c4a49ad8c0a33ed899b60548d21a62971a8e994cafc0e9fbc710a0a220b055804bb890833b50ac04ec4e130a5db75338c0c1d49a52a6d373076a5db370564a5cebb5300f79877003c52adf49dac16370e51e14e0754110547bb3b";
        System.out.println(cipherText);
        System.out.println("解密: ");
        plainText = new String(SM2EncDecUtils.decrypt(Util.hexToByte(prik), Util.hexToByte(cipherText)));
        System.out.println(plainText);

    }

    public void sm3() {
        byte[] md = new byte[32];
        byte[] msg1 = "ererfeiisgod".getBytes();
        System.out.println(Util.byteToHex(msg1));
        SM3Digest sm3 = new SM3Digest();
        sm3.update(msg1, 0, msg1.length);
        sm3.doFinal(md, 0);
        String s = new String(Hex.encode(md));
        System.out.println(s.toUpperCase());
    }

    public void sm4() throws IOException {
        /*String plainText = "我爱中国啊1我爱我我他2";
        String s = Util.byteToHex(plainText.getBytes());
        System.out.println("原文" + s);
        SM4Utils sm4 = new SM4Utils();
        //sm4.secretKey = "JeF8U9wHFOMfs2Y8";
        sm4.secretKey = "E76E9B4E0245BC56FCE4E29B208C6A50";
        sm4.hexString = true;

        System.out.println("ECB模式加密");
        String cipherText = sm4.encryptData_ECB(plainText);
        System.out.println("密文: " + cipherText);
        System.out.println("");

        plainText = sm4.decryptData_ECB(cipherText);
        System.out.println("明文: " + plainText);
        System.out.println("");

        System.out.println("CBC模式加密");
        sm4.iv = "30303030303030303030303030303030";
        cipherText = sm4.encryptData_CBC(plainText);
        System.out.println("加密密文: " + cipherText);
        System.out.println("");

        plainText = sm4.decryptData_CBC(cipherText);
        System.out.println("解密明文: " + plainText);*/

    }


    public static void main(String[] args) throws IOException {


    }
}

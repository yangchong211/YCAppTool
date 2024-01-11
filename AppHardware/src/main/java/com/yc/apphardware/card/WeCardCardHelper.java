package com.yc.apphardware.card;

import android.text.TextUtils;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 微卡实体卡工具类
 */
public class WeCardCardHelper {


    /**
     * 获取实体卡选取指令
     */
    public static String getSelectFileOrder() {
        return "00a40000023f01";
    }


    /**
     * 获取卡生成随机数指令
     */
    public static String getRandomOrder() {
        return "0084000004";
    }


    /**
     * 使用4位随机数生成带有MAC检验的读取CPU卡16文件的指令
     *
     * @param lineProtectionKey 实体卡线路保护秘钥
     * @param randomByteArray 4位随机数
     * @return 带MAC检验读取16文件的指令
     */
    public static String generateReadCardOrder(String lineProtectionKey, byte[] randomByteArray) {
        try {
            //把生成密匙分成俩个部分，后期用来继续加密
            String left = lineProtectionKey.substring(0, 16);
            String right = lineProtectionKey.substring(16, 32);
            // APDU获取4字节随机数
            ByteBuffer randomByte = ByteBuffer.allocate(8);
            randomByte.put(randomByteArray, 0, 4);
            randomByte.put((byte) 0x00);
            randomByte.put((byte) 0x00);
            randomByte.put((byte) 0x00);
            randomByte.put((byte) 0x00);
            // 随机数后补“0x00000000”，得到的 8 字节结果作为 MAC 计算的初始值
            byte[] MACInitData = randomByte.array();
            // 指令 0x04,0xB0,0x96,0x00,0x04 后补 0x80,0x00， 0x00 凑够 8 的整数倍，作为数据块 D1
            byte[] D1_Data = new byte[]{0x04, (byte) 0xB0, (byte) 0x96, 0x00, 0x04, (byte) 0x80, (byte) 0x00, 0x00};
            // 计算 MAC
            // 1、MAC计算的初始值 与 D1数据库 异或
            byte[] result_1 = new byte[8];
            for (int i = 0; i < 8; i++) {
                result_1[i] = (byte) (MACInitData[i] ^ D1_Data[i]);
            }
            String result_1_HEX = bytesToHex(result_1);
            // 2、用16字节秘钥的左半部分进行DES机密
            String encryptDES = encryptDES(result_1_HEX, left);
            // 3、用16字节秘钥的右半部分进行DES解密
            String cryptDES = decryptDES(encryptDES, right);
            // 4、用16字节秘钥的左半部分进行DES机密 ,得到8字节的计算结果
            String resultMAC = encryptDES(cryptDES, left);
            // 取计算结果的前 4 个字节作为 MAC , 0x04,0xB0,0x96,0x00,0x04, MAC
            return "04b0960004" + resultMAC.substring(0, 8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }


    /**
     * 卡号高低位转换处理
     *
     * @param cardNum 卡号
     * @return 处理后的卡号
     */
    public static String flipCardNum(String cardNum) {
        if (!TextUtils.isEmpty(cardNum) && cardNum.length() == 8) {
            cardNum = cardNum.substring(6, 8) + cardNum.substring(4, 6) + cardNum.substring(2, 4) + cardNum.substring(0, 2);
            cardNum = cardNum.toLowerCase();
        }
        return cardNum;
    }


    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String bytesToHex(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for (byte b : bytes) { // 使用除与取余进行转换
            if (b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }

            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }

        return new String(buf);
    }

    /**
     * 加密
     */
    private static String encryptDES(String encryptString, String encryptKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(toBytes(encryptKey), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(toBytes(encryptString));
        return bytesToHex(encryptedData);
    }

    /**
     * 解密
     */
    private static String decryptDES(String decryptString, String decryptKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(toBytes(decryptKey), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal(toBytes(decryptString));
        return bytesToHex(decryptedData);
    }


    private static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("") || !isHexString(str)) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        try {
            for (int i = 0; i < str.length() / 2; i++) {
                String subStr = str.substring(i * 2, i * 2 + 2);
                bytes[i] = (byte) Integer.parseInt(subStr, 16);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 判断一个字符串是否有16进制
     */
    private static boolean isHexString(String s) {
        Pattern p = Pattern.compile("^[A-Fa-f0-9]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

}

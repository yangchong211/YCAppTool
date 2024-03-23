package com.yc.toolutils.bytes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字节数组和16进制转化工具类
 */
public final class BytesHexStrUtils {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    public static byte[] changeBytes(byte[] a) {
        byte[] b = new byte[a.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = a[b.length - i - 1];
        }
        return b;
    }


    /**
     * 方法功能：判断一个字符串是否有16进制
     *
     * @param s 内容
     * @return boolean
     */
    public static boolean isHexString(String s) {
        Pattern p = Pattern.compile("^[A-Fa-f0-9]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }


    /**
     * 将byte[]转换为16进制字符串
     *
     * @param bytes 字节数组
     * @return 16进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for (byte b : bytes) {
            // 使用除与取余进行转换
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
     * 字节数组转转hex字符串
     *
     * @param bytes 字节数组
     * @return hex字符串
     */
    public static String bytesToHex2(byte[] bytes) {
        StringBuilder strBuilder = new StringBuilder();
        int j = bytes.length;
        for (int i = 0; i < j; i++) {
            strBuilder.append(Byte2Hex(bytes[i]));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    /**
     * 字节数组转转hex字符串，可选长度
     *
     * @param inBytArr
     * @param offset
     * @param byteCount
     * @return
     */
    public static String bytesToHex2(byte[] inBytArr, int offset, int byteCount) {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++) {
            strBuilder.append(Byte2Hex(inBytArr[i]));
        }
        return strBuilder.toString();
    }


    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str 字符串
     * @return byte
     */
    public static byte[] hexToBytes2(String str) {
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
     * hex字符串转字节数组
     *
     * @param inHex hex字符串
     * @return          字节数组
     */
    public static byte[] hexToBytes1(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen) == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str 字符串
     * @return
     */
    public static byte[] hexToBytes3(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    /**
     * 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
     *
     * @param num 数据
     * @return 奇偶
     */
    public static int isOdd(int num) {
        return num & 0x1;
    }

    /**
     * Hex字符串转byte
     *
     * @param inHex
     * @return
     */
    public static byte HexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    /**
     * Hex字符串转int
     *
     * @param inHex
     * @return
     */
    public static int HexToInt(String inHex) {
        return Integer.parseInt(inHex, 16);
    }

    /**
     * 1字节转2个Hex字符
     *
     * @param inByte byte
     * @return
     */
    public static String Byte2Hex(Byte inByte) {
        return String.format("%02x", inByte).toUpperCase();
    }
}

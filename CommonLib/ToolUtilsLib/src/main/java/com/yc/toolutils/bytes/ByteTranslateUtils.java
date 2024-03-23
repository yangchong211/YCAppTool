package com.yc.toolutils.bytes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ByteTranslateUtils {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 建议使用第一种
     * 将字节数组转化为十六位字符串，第一种
     * @param bytes     字节数组
     * @return
     */
    public static String bytesToHex1(byte[] bytes) {
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
     * 将字节数组转化为十六位字符串，第二种
     * @param data     字节数组
     * @return
     */
    public static String bytesToHex2(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 将字节数组转化为十六位字符串，第三种
     * 手动实现字节数组转换为十六进制字符串的算法，需要考虑多种特殊情况。
     * 例如，需要注意字节顺序、字符串长度等问题。建议在实际开发中使用成熟的开源库，而不是手动实现算法。
     * @param data     字节数组
     * @return
     */
    public static String bytesToHex3(byte[] data) {
        //使用 StringBuilder 类来拼接最终的十六进制字符串
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            //在循环处理每个字节时，我们使用 Integer.toHexString() 方法将其转换为一个长度为 2 的十六进制字符串
            String hexString = Integer.toHexString(b & 0xFF);
            //注意补充前导零（如果需要）
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            //将每个字符串拼接在一起，得到最终的十六进制字符串 hexString
            sb.append(hexString);
        }
        return sb.toString();
    }

    /**
     * 方法功能：判断一个字符串是否有16进制
     * @param s 内容
     * @return boolean
     * */
    public static boolean isHexString(String s){
        Pattern p = Pattern.compile("^[A-Fa-f0-9]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 将16进制字符串转换为byte[]
     * @param str 字符串
     * @return byte
     */
    public static byte[] toBytes1(String str) {
        if (str == null || str.trim().equals("") || !isHexString(str)) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        try {
            for (int i = 0; i < str.length() / 2; i++) {
                String subStr = str.substring(i * 2, i * 2 + 2);
                bytes[i] = (byte) Integer.parseInt(subStr, 16);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static byte[] toBytes2(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen) == 1) {
            ++hexlen;
            result = new byte[hexlen / 2];
            inHex = "0" + inHex;
        } else {
            result = new byte[hexlen / 2];
        }
        int j = 0;
        for(int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            ++j;
        }
        return result;
    }

    private static int isOdd(int num) {
        return num & 1;
    }

    private static byte hexToByte(String inHex) {
        return (byte)Integer.parseInt(inHex, 16);
    }
}

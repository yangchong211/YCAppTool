package com.yc.toolutils.bytes;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 字节转化工具类
 *     revise:
 * </pre>
 */
public final class ByteEasyUtils {

    /**
     * bit = 0/1       就是一个二进制数，这是计算机运算的最小单位，也是最基本的单位，什么都会转为01来计算
     * byte  = 8bit    byte也就是字节。是文件存储的最小单位。也就是说一个字节 = 8个二进制数
     *
     * AscII编码：unicode 存储英文和标点就是一个字节 1 byte 存中文就要2byte.
     * UTF-8 ：存英文要1个byte,存中文要3个byte。
     * Unicode编码：一个英文等于两个字节，一个中文（含繁体）等于两个字节。
     *
     * java采用unicode
     *
     * Byte 字节， 8位；
     * Boolean ，1个字节
     * Short ， 2个字节，16位；
     * char ,2个字节，16位；
     * Int ， 4个字节，32位；
     * float， 4个字节，32位；
     * Long ，8个字节，64位；
     * double，8个字节，64位；
     */


    /**
     * byte b = 0x35; // 0011 0101
     * // 输出 [0, 0, 1, 1, 0, 1, 0, 1]
     * System.out.println(Arrays.toString(getBooleanArray(b)));
     * // 输出 00110101
     * System.out.println(byteToBit(b));
     * // JDK自带的方法，会忽略前面的 0
     * System.out.println(Integer.toBinaryString(0x35));
     * <p>
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     */
    public static byte[] getBooleanArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    /**
     * 把byte转为字符串的bit
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    /**
     * 二进制字符串转byte
     * ByteUtils.decodeBinaryString("00110100")
     * 输出：  0x34
     */
    public static byte decodeBinaryString(String byteStr) {
        int re, len;
        if (null == byteStr) {
            return 0;
        }
        len = byteStr.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        // 8 bit处理
        if (len == 8) {
            if (byteStr.charAt(0) == '0') {
                // 正数
                re = Integer.parseInt(byteStr, 2);
            } else {
                // 负数
                re = Integer.parseInt(byteStr, 2) - 256;
            }
        } else {
            // 4 bit处理
            re = Integer.parseInt(byteStr, 2);
        }
        return (byte) re;
    }


}

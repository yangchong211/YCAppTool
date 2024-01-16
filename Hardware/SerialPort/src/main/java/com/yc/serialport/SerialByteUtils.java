package com.yc.serialport;

public class SerialByteUtils {
    private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * 字符串转字节数组
     *
     * @param s 字符串
     * @return 数组
     */
    public static byte[] hexStringToByteArray(String s) {
        if (s.length() < 2) {
            s = "0" + s;
        }
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 字节数组转字符串
     *
     * @param hexBytes 数组
     * @return 字符串
     */
    public static String hexBytesToString(byte[] hexBytes) {
        char[] hexChars = new char[hexBytes.length * 2];
        for (int j = 0; j < hexBytes.length; j++) {
            int v = hexBytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * 16进制字符串转int
     *
     * @param hexString 字符串
     * @return int
     */
    public static int hexStringToInt(String hexString) {
        return Integer.parseInt(hexString, 16);
    }

    /**
     * 16进制字符串转byte数组
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * char转换位byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}

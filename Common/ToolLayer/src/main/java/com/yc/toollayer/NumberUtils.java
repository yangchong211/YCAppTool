package com.yc.toollayer;

import java.text.DecimalFormat;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/6/12
 *     desc  : 转化工具类
 *     revise:
 * </pre>
 */
public final class NumberUtils {

    public static int parse(String string) {
        if (string == null || string.length() == 0) {
            return -1;
        }
        if (isInteger(string)) {
            return Integer.parseInt(string);
        } else if (isFloat(string)) {
            return (int) Float.parseFloat(string);
        } else if (isDouble(string)) {
            return (int) Double.parseDouble(string);
        }
        return -1;
    }

    public static long parseLong(String string) {
        if (string == null || string.length() == 0) {
            return -1;
        }
        if (isInteger(string)) {
            return Integer.parseInt(string);
        } else if (isFloat(string)) {
            return (long) Float.parseFloat(string);
        } else if (isDouble(string)) {
            return (long) Double.parseDouble(string);
        } else if (isLong(string)) {
            return Long.parseLong(string);
        }
        return -1;
    }

    public static double parseDouble(String string) {
        if (string == null || string.length() == 0) {
            return -1;
        }
        if (isInteger(string)) {
            return Integer.parseInt(string);
        } else if (isFloat(string)) {
            return Float.parseFloat(string);
        } else if (isDouble(string)) {
            return Double.parseDouble(string);
        }
        return -1;
    }

    private static boolean isInteger(String string) {
        boolean isInteger;
        try {
            Integer.parseInt(string);
            isInteger = true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            isInteger = false;
        }
        return isInteger;
    }

    private static boolean isFloat(String string) {
        boolean isFloat;
        try {
            Float.parseFloat(string);
            isFloat = true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            isFloat = false;
        }
        return isFloat;
    }

    private static boolean isDouble(String string) {
        boolean isDouble;
        try {
            Double.parseDouble(string);
            isDouble = true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            isDouble = false;
        }
        return isDouble;
    }

    private static boolean isLong(String string) {
        boolean isLong;
        try {
            Long.parseLong(string);
            isLong = true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            isLong = false;
        }
        return isLong;
    }


    /**
     * 测试：10000.00---100056.11----70.00-----10,000000.00-----10000000.00
     * 10,000.00---100,056.11----70.00-----10,000,000.04-----10,000,000.00
     * 格式化数字为千分位，保留两位小数
     * <p>
     * float：2^23 = 8388608，共七位，意味着最多能有7位有效数字，但绝对能保证的为6位，也即float的精度为6~7位有效数字；
     * double：2^52 = 4503599627370496，一共16位，同理，double的精度为15~16位。
     *
     * @param text 数字
     * @return String           返回类型
     */
    public static String fmtMicrometer(String text) {
        if (text == null || text.length() == 0) {
            return "00.00";
        }
        String num;
        if (isFloat(text)) {
            double v = Double.parseDouble(text);
            num = fmtMicrometer(v);
        } else if (isDouble(text)) {
            double v = Double.parseDouble(text);
            num = fmtMicrometer(v);
        } else if (isInteger(text)) {
            int i = Integer.parseInt(text);
            num = fmtMicrometer(i / 1.0f);
        } else if (isLong(text)) {
            long l = Long.parseLong(text);
            num = fmtMicrometer(l / 1.0f);
        } else {
            num = "00.00";
        }
        return num;
    }


    /**
     * 测试：10000.00---100056.11----70.00-----10,000000.00-----10000000.00
     * 10,000.00---100,056.11----70.00-----10,000,000.00-----10,000,000.00
     * 格式化数字为千分位，保留两位小数
     *
     * @param num 数字
     * @return String           返回类型
     */
    public static String fmtMicrometer(double num) {
        if (num != 0) {
            DecimalFormat df = new DecimalFormat("###,##0.00");
            //LogUtils.e("返回类型"+df.format(num));
            return df.format(num);
        } else {
            return "0.00";
        }
    }
}

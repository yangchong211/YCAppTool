package com.yc.location.easymock;

public final class LocationToolUtils {

    public static boolean DEBUG = true;

    public static void log(String msg) {
        d("LocationUtils", msg);
    }

    /**
     * 打印debug级别信息
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if ($(msg)) {
            return;
        }
        if (tag == null){
            tag = "LocationUtils";
        }
        if (DEBUG) {
            android.util.Log.d(tag, msg);
        }
    }

    private static boolean $(String msg) {
        return msg == null;
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

}

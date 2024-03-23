package com.yc.toolutils.string;

import android.text.TextUtils;

import java.security.MessageDigest;

public final class AppStringUtils {



    public static String ellipsize(String input, int maxCharacters, int charactersAfterEllipsis) {
        if (TextUtils.isEmpty(input)) {
            return input;
        }
        if (input.length() <= maxCharacters) {
            return input;
        }

        String ellipsis = "...";
        int ellipsisLen = 1;
        if (maxCharacters < ellipsisLen) {
            throw new IllegalArgumentException(
                    "maxCharacters must be at least 1 because the ellipsis already take up 1 characters");
        }
        return input.substring(0, maxCharacters - ellipsisLen - charactersAfterEllipsis)
                + ellipsis
                + input.substring(input.length() - charactersAfterEllipsis);
    }



    /**
     * 将字符串转成MD5值
     *
     * @param string
     * @return
     */
    public static String MD5(String string) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (Throwable e) {
            return "";
        }
    }

}

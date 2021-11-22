package com.yc.logging.util;

import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Iterator;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class StringUtils {


    public static String dumpBundleArray(Bundle[] bundleArray) {
        if (bundleArray == null) {
            return "null";
        }
        StringBuilder content = new StringBuilder("[");
        for (int i = 0; i < bundleArray.length; i++) {
            content.append(dumpBundle(bundleArray[i]));
            if (i < bundleArray.length - 1) {
                content.append(", ");
            }
        }
        content.append("]");
        return content.toString();
    }

    public static String dumpBundle(Bundle bundle) {
        if (bundle == null) {
            return "null";
        }
        StringBuilder content = new StringBuilder("{");
        Iterator<String> it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            content.append(key).append("=");
            Object obj = bundle.get(key);
            String val;
            if (obj instanceof Bundle) {
                if (obj == bundle) {
                    val = "{this}";
                } else {
                    val = dumpBundle((Bundle) obj);
                }
            }//
            else if (obj instanceof Bundle[]) {
                val = dumpBundleArray((Bundle[]) obj);
            }//
            else if (obj instanceof Object[]) {
                val = Arrays.toString((Object[]) obj);
            }//
            else {
                val = String.valueOf(obj);
            }
            content.append(val);
            if (it.hasNext()) {
                content.append(", ");
            }
        }
        content.append("}");
        return content.toString();
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

    public static String ellipsize(String input, int maxCharacters, int charactersAfterEllipsis) {
        if (TextUtils.isEmpty(input)) return input;
        if (input.length() <= maxCharacters) return input;

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
}

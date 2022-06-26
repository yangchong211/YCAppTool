package com.yc.intent.utils;

import android.os.Bundle;

import java.util.Arrays;
import java.util.Iterator;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : intent工具类
 *     revise: 之前搜车封装库
 * </pre>
 */
public final class IntentLogUtils {

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
}

package com.yc.toolutils.intent;

import android.os.Bundle;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author yangchong
 * GitHub : <a href="https://github.com/yangchong211/YCCommonLib">...</a>
 * email : yangchong211@163.com
 * time  : 2018/11/9
 * desc  : intent工具类
 * revise: 之前搜车封装库
 * 通用打印Intent对象内容到log日志栏中，支持普通intent和延迟PendingIntent。超级方便检查，可以打印action，category，data，flags，extras等等
 * //打印intent所有的数据
 * IntentLogger.print("intent test : ", intent)
 * //打印intent中component
 * IntentLogger.printComponentName("intent component : " , intent)
 * //打印intent中extras参数
 * IntentLogger.printExtras("intent test : ", intent)
 * //打印intent中flags参数
 * IntentLogger.printFlags("intent test : ", intent)
 * //PendingIntent
 * //打印intent所有的数据
 * PendingIntentLogger.print("intent test : ", intent)
 * //打印intent中content
 * PendingIntentLogger.printContentIntent("intent content : " , intent)
 * //打印intent的tag
 * PendingIntentLogger.printTag("intent tag : " , intent)
 */
public final class IntentLogUtils {

    private static String dumpBundleArray(Bundle[] bundleArray) {
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

    /**
     * 打印bundle数据
     *
     * @param bundle bundle数据
     * @return 字符串
     */
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
            } else if (obj instanceof Bundle[]) {
                val = dumpBundleArray((Bundle[]) obj);
            } else if (obj instanceof Object[]) {
                val = Arrays.toString((Object[]) obj);
            } else {
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

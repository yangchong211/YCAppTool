package com.ycbjie.webviewlib.utils;

import android.text.TextUtils;

import java.util.HashMap;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 是否重复点击工具类
 *     revise: demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public final class FastClickUtils {

    private static final int MAX_INTERVAL = 1000;
    private static long mLastClickTime;
    private static HashMap<String, Long> tagMaps = new HashMap<>();

    /**
     * 判断一个控件是否短时间内重复点击
     * @return                                  是否重复点击
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(MAX_INTERVAL);
    }

    /**
     * 判断一个控件是否短时间内重复点击
     * @param maxInterval                       时长
     * @return                                  是否重复点击
     */
    public static boolean isFastDoubleClick(int maxInterval) {
        long current = System.currentTimeMillis();
        long interval = current - mLastClickTime;
        if ((interval > 0) && (interval < maxInterval)) {
            return true;
        }
        mLastClickTime = current;
        return false;
    }

    /**
     * 判断一个控件是否短时间内重复点击
     * @param maxInterval                       时长
     * @param tag                               标签
     * @return                                  是否重复点击
     */
    public static boolean isFastDoubleClickWithTag(int maxInterval, String tag) {
        if (TextUtils.isEmpty(tag)) {
            return true;
        }
        long current = System.currentTimeMillis();
        long interval = 0;
        if (tagMaps.containsKey(tag)) {
            // 获取上次保存时间离现在的时间间距.
            Long aLong = tagMaps.get(tag);
            //noinspection ConstantConditions
            interval = current - aLong;
        }
        if ((interval > 0) && (interval < maxInterval)) {
            return true;
        }
        // 放入当前tag对应的时间
        tagMaps.put(tag, current);
        return false;
    }

}

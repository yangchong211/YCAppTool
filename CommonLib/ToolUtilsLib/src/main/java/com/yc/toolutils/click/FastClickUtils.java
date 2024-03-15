package com.yc.toolutils.click;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/23
 *     desc  : 快速点击判断
 *     revise:
 * </pre>
 */
public final class FastClickUtils {

    /**
     * 默认最大点击间隔时间
     */
    private static final int MAX_INTERVAL = 500;
    /**
     * 最后一次点击的时间戳
     */
    private static long mLastClickTime;
    /**
     * tag标记的集合
     */
    private static final HashMap<String, Long> tagMaps = new HashMap<>();

    /**
     * 判断一个控件是否短时间内重复点击
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(MAX_INTERVAL);
    }

    /**
     * 判断一个控件是否xx时间内重复点击
     * @param maxInterval           设置间隔时间
     * @return                      true表示是重复点击
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
     * 判断一个控件是否xx时间内重复点击
     * @param maxInterval           设置间隔时间
     * @param tag                   标记
     * @return                      true表示是重复点击
     */
    public static boolean isFastDoubleClickWithTag(int maxInterval, String tag) {
        if (TextUtils.isEmpty(tag)) {
            return true;
        }
        long current = System.currentTimeMillis();
        long interval = 0;
        if (tagMaps.containsKey(tag)) {
            // 获取上次保存时间离现在的时间间距.
            interval = current - tagMaps.get(tag);
        }
        if ((interval > 0) && (interval < maxInterval)) {
            return true;
        }
        // 放入当前tag对应的时间
        tagMaps.put(tag, current);
        return false;
    }
}

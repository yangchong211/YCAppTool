package com.ycbjie.live.config;

import android.content.Context;
import android.content.Intent;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 前台服务通知点击事件
 *     revise:
 * </pre>
 */
public interface ForegroundNotificationClickListener {

    /**
     * 前台服务被点击
     *
     * @param context
     * @param intent
     */
    void onNotificationClick(Context context, Intent intent);
}

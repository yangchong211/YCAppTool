package com.ycbjie.library.inter.listener;

import android.view.View;

import java.util.Calendar;


/**
 * <pre>
 *     @author yangchong
 *     blog  :
 *     time  : 2016/6/11.
 *     desc  : 防止重复点击的点击监听
 *     revise: 经过测试没有问题
 * </pre>
 */
public abstract class NoDoubleClickListener implements View.OnClickListener {

    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        int MIN_CLICK_DELAY_TIME = 1000;
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    protected abstract void onNoDoubleClick(View view);
}

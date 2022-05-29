package com.yc.toolutils.click;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/23
 *     desc  : n次点击后触发点击事件
 *     revise:
 * </pre>
 */
public abstract class CountClickListener implements View.OnClickListener {

    private final long targetCount;
    private int clickCount = 0;
    private static final int MSG_RESET_COUNT = 1;
    private static final int DELAY_TIME = 1000;

    private final Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_RESET_COUNT){
                clickCount = 0;
            }
        }
    };

    public CountClickListener(long targetCount){
        if (targetCount < 0){
            throw new IllegalStateException("please set count > 0");
        }
        this.targetCount = targetCount;
    }

    @Override
    public void onClick(View v) {
        clickCount++;
        if (clickCount > targetCount) {
            onCountClick();
            clickCount = 0;
        }
        handler.removeMessages(MSG_RESET_COUNT);
        handler.sendEmptyMessageDelayed(MSG_RESET_COUNT, DELAY_TIME);
    }

    /**
     * 触发回调
     */
    protected abstract void onCountClick();
}

package com.yc.toolutils.click;


import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.util.LinkedList;

public final class ClickHelper {

    private final String TAG = ClickHelper.class.getSimpleName();
    private static final ClickHelper INSTANCE = new ClickHelper();
    private final LinkedList<Long> clickList = new LinkedList<>();
    /**
     * 检测多次按下的时间间隔
     */
    private int TIME_SEP = 300;
    private final Handler handler;

    private ClickHelper() {
        handler = new Handler(Looper.getMainLooper());
    }

    public static ClickHelper getInstance() {
        return ClickHelper.INSTANCE;
    }

    public void setTimeSep(int timeSep){
        if (timeSep < 0){
            return;
        }
        TIME_SEP = timeSep;
    }

    public void onClickKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        //有单击就清理定时器
        clearHandle();
        //判断三连击
        if (clickList.size() == 3) {
            clickList.removeFirst();
        }
        clickList.add(System.currentTimeMillis());
        Log.d(TAG, "click add :" + System.currentTimeMillis() + " size:" + clickList.size());
        handler.postDelayed(() -> {
            //检测是否发生三连击
            if (clickList.size() == 3) {
                long fistSep = (long) clickList.get(1) - clickList.get(0);
                long secondSep = (long) clickList.get(2) - clickList.get(1);
                Log.d(TAG, "fistSep :" + fistSep + "secondSep：" + secondSep);
                if (fistSep < TIME_SEP && secondSep < TIME_SEP) {
                    //判定为三连击
                    onMultipleClick(key);
                }
            } else {
                if (clickList.size() < 3) {
                    //单击行为
                    onSingleClick(key);
                } else {
                    Log.d(TAG, "click error " + key);
                }
            }
            //最后一定会清掉
            clickList.clear();
        }, TIME_SEP);
    }

    public void onThreeClick(String key) {
        onClickKey(key, 3);
    }

    public void onClickKey(String key, int clickNum) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        //有单击就清理定时器
        clearHandle();
        //判断三连击
        if (clickList.size() == clickNum) {
            clickList.removeFirst();
        }
        clickList.add(System.currentTimeMillis());
        Log.d(TAG, "click add :" + System.currentTimeMillis() + " size:" + clickList.size());
        handler.postDelayed(() -> {
            //检测是否发生三连击
            if (clickList.size() == clickNum) {
                boolean isMultipleClick = true;
                for (int i = 0; (i < clickNum - 1); i++) {
                    long sep = (long) clickList.get(i + 1) - (long) clickList.get(i);
                    //只要有一个不符合条件，则就不是num多次连击
                    if (sep > TIME_SEP) {
                        isMultipleClick = false;
                        break;
                    }
                }
                if (isMultipleClick) {
                    onMultipleClick(key);
                }
            } else {
                if (clickList.size() < clickNum) {
                    //单击行为
                    onSingleClick(key);
                } else {
                    Log.d(TAG, "click error " + key);
                }
            }
            //最后一定会清掉
            clickList.clear();
        }, TIME_SEP);
    }


    private void onSingleClick(String key) {
        if (onClickListener != null) {
            onClickListener.onSingleClick(key);
        }
    }

    private void onMultipleClick(String key) {
        if (onClickListener != null) {
            onClickListener.onMultipleClick(key);
        }
    }

    /**
     * 清除消息队列
     */
    private void clearHandle() {
        Log.d(TAG, "clearHandle：清理定时器");
        handler.removeCallbacksAndMessages(null);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener clickListener) {
        this.onClickListener = clickListener;
    }

    public interface OnClickListener {
        void onSingleClick(String key);

        void onMultipleClick(String key);
    }
}

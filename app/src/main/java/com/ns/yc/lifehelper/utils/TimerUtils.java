package com.ns.yc.lifehelper.utils;

import android.os.CountDownTimer;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/7/3
 * 描    述：倒计时工具类
 * 修订历史：
 * ================================================
 */
public class TimerUtils {



    public static TimeCount timeCount;
    /**
     * 第二种倒计时，用系统自带的，可以在销毁
     * @return
     */
    public static TimeCount setTimer(int time){
        timeCount = new TimeCount(time*1000, 1000);
        timeCount.start();
        return timeCount;
    }

    public static class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);   //参数依次为总时长,和计时的时间间隔

        }
        @Override
        public void onFinish() {                            //计时完毕时触发

        }

        @Override
        public void onTick(long millisUntilFinished) {       //计时过程显示

        }
    }

}

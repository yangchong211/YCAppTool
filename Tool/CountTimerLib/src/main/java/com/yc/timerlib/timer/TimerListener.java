package com.yc.timerlib.timer;


/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time  :  2020/5/26
 *     desc  :  倒计时监听器
 *     revise:
 * </pre>
 */
public interface TimerListener {

    /**
     * 当倒计时开始
     */
    void onStart();

    /**
     * 当倒计时结束
     */
    void onFinish();

    /**
     * @param millisUntilFinished 剩余时间
     */
    void onTick(long millisUntilFinished);

}

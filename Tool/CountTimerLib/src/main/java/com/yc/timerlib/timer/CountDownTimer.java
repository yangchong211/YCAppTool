package com.yc.timerlib.timer;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import androidx.annotation.NonNull;

/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time  :  2020/5/26
 *     desc  :  自定义倒计时器
 *     revise:
 * </pre>
 */
public class CountDownTimer {

    /**
     * 时间，即开始的时间，通俗来说就是倒计时总时间
     */
    private long mMillisInFuture;
    /**
     * 布尔值，表示计时器是否被取消
     * 只有调用cancel时才被设置为true
     */
    private boolean mCancelled = false;
    /**
     * 用户接收回调的时间间隔，一般是1秒
     */
    private long mCountdownInterval;
    /**
     * 记录暂停时候的时间
     */
    private long mStopTimeInFuture;
    /**
     * mas.what值
     */
    private static final int MSG = 520;
    /**
     * 暂停时，当时剩余时间
     */
    private long mCurrentMillisLeft;
    /**
     * 是否暂停
     * 只有当调用pause时，才设置为true
     */
    private boolean mPause = false;
    /**
     * 监听listener
     */
    private TimerListener mCountDownListener;
    /**
     * 是否创建开始
     */
    private boolean isStart;

    public CountDownTimer(){
        isStart = true;
    }

    public CountDownTimer(long millisInFuture, long countdownInterval) {
        long total = millisInFuture + 20;
        this.mMillisInFuture = total;
        //this.mMillisInFuture = millisInFuture;
        this.mCountdownInterval = countdownInterval;
        isStart = true;
    }

    /**
     * 开始倒计时，每次点击，都会重新开始
     */
    public synchronized final void start() {
        if (mMillisInFuture <= 0 && mCountdownInterval <= 0) {
            throw new RuntimeException("you must set the millisInFuture > 0 or countdownInterval >0");
        }
        mCancelled = false;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        mStopTimeInFuture = elapsedRealtime + mMillisInFuture;
        CountTimeTools.i("start → mMillisInFuture = " + mMillisInFuture + ", seconds = " + mMillisInFuture / 1000 );
        CountTimeTools.i("start → elapsedRealtime = " + elapsedRealtime + ", → mStopTimeInFuture = " + mStopTimeInFuture);
        mPause = false;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        if (mCountDownListener!=null){
            mCountDownListener.onStart();
        }
    }

    /**
     * 取消计时器
     */
    public synchronized final void cancel() {
        if (mHandler != null) {
            //暂停
            mPause = false;
            mHandler.removeMessages(MSG);
            //取消
            mCancelled = true;
        }
    }

    /**
     * 按一下暂停，再按一下继续倒计时
     */
    public synchronized final void pause() {
        if (mHandler != null) {
            if (mCancelled) {
                return;
            }
            if (mCurrentMillisLeft < mCountdownInterval) {
                return;
            }
            if (!mPause) {
                mHandler.removeMessages(MSG);
                mPause = true;
            }
        }
    }

    /**
     * 恢复暂停，开始
     */
    public synchronized final  void resume() {
        if (mMillisInFuture <= 0 && mCountdownInterval <= 0) {
            throw new RuntimeException("you must set the millisInFuture > 0 or countdownInterval >0");
        }
        if (mCancelled) {
            return;
        }
        //剩余时长少于
        if (mCurrentMillisLeft < mCountdownInterval || !mPause) {
            return;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mCurrentMillisLeft;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        mPause = false;
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            synchronized (CountDownTimer.this) {
                if (mCancelled) {
                    return;
                }
                //剩余毫秒数
                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();
                if (millisLeft <= 0) {
                    mCurrentMillisLeft = 0;
                    if (mCountDownListener != null) {
                        mCountDownListener.onFinish();
                        CountTimeTools.i("onFinish → millisLeft = " + millisLeft);
                    }
                } else if (millisLeft < mCountdownInterval) {
                    mCurrentMillisLeft = 0;
                    CountTimeTools.i("handleMessage → millisLeft < mCountdownInterval !");
                    // 剩余时间小于一次时间间隔的时候，不再通知，只是延迟一下
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    //有多余的时间
                    long lastTickStart = SystemClock.elapsedRealtime();
                    CountTimeTools.i("before onTick → lastTickStart = " + lastTickStart);
                    CountTimeTools.i("before onTick → millisLeft = " + millisLeft + ", seconds = " + millisLeft / 1000 );
                    if (mCountDownListener != null) {
                        mCountDownListener.onTick(millisLeft);
                        CountTimeTools.i("after onTick → elapsedRealtime = " + SystemClock.elapsedRealtime());
                    }
                    mCurrentMillisLeft = millisLeft;
                    // 考虑用户的onTick需要花费时间,处理用户onTick执行的时间
                    // 打印这个delay时间，大概是997毫秒
                    long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();
                    CountTimeTools.i("after onTick → delay1 = " + delay);
                    // 特殊情况：用户的onTick方法花费的时间比interval长，那么直接跳转到下一次interval
                    // 注意，在onTick回调的方法中，不要做些耗时的操作
                    boolean isWhile = false;
                    while (delay < 0){
                        delay += mCountdownInterval;
                        isWhile = true;
                    }
                    if (isWhile){
                        CountTimeTools.i("after onTick执行超时 → delay2 = " + delay);
                    }
                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };

    /**
     * 设置倒计时总时间
     * @param millisInFuture                    毫秒值
     */
    public void setMillisInFuture(long millisInFuture) {
        long total = millisInFuture + 20;
        this.mMillisInFuture = total;
    }

    /**
     * 设置倒计时间隔值
     * @param countdownInterval                 间隔，一般设置为1000毫秒
     */
    public void setCountdownInterval(long countdownInterval) {
        this.mCountdownInterval = countdownInterval;
    }

    /**
     * 设置倒计时监听
     * @param countDownListener                 listener
     */
    public void setCountDownListener(TimerListener countDownListener) {
        this.mCountDownListener = countDownListener;
    }

}

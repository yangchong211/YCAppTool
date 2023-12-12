package com.yc.timerlib.view;


import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yc.timerlib.timer.CountDownTimer;
import com.yc.timerlib.timer.CountTimeTools;
import com.yc.timerlib.timer.TimerListener;

/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time  :  2020/5/26
 *     desc  :  自定义倒计时器文本
 *     revise:
 * </pre>
 */
public class TimerTextView extends AppCompatTextView {

    public long mCountDownInterval = 1000;
    /**
     * 时间，即开始的时间，通俗来说就是倒计时总时间
     */
    private long mMillisInFuture;
    /**
     * 是否展示文本
     */
    private boolean isAutoShowText = false;
    private CountDownCallback countDownCallback;
    private CountDownTimer mCountDownTimer;

    public TimerTextView(Context context) {
        super(context);
        init();
    }

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancel();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }

    /**
     * 开始倒计时，每次点击，都会重新开始
     */
    public void start() {
        startCountDown();
    }

    /**
     * 取消计时器
     */
    public void cancel() {
        if (mCountDownTimer!=null){
            mCountDownTimer.cancel();
        }
    }

    /**
     * 按一下暂停，再按一下继续倒计时
     */
    public void pause(){
        if (mCountDownTimer!=null){
            mCountDownTimer.pause();
        }
    }

    /**
     * 恢复暂停，开始
     */
    public void resume(){
        if (mCountDownTimer!=null){
            mCountDownTimer.resume();
        }
    }

    public interface CountDownCallback {

        void onTick(TimerTextView countDownTextView, long millisUntilFinished);

        void onFinish(TimerTextView countDownTextView);

    }

    private synchronized void updateText(long now) {
        String text = CountTimeTools.getCountTime(now);
        setText(text);
    }

    /**
     * 触发初始化，以及开始倒计时
     */
    private void startCountDown(){
        mCountDownTimer = new CountDownTimer();
        mCountDownTimer.setMillisInFuture(mMillisInFuture);
        mCountDownTimer.setCountdownInterval(mCountDownInterval);
        mCountDownTimer.setCountDownListener(new TimerListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
                if (countDownCallback != null) {
                    countDownCallback.onFinish(TimerTextView.this);
                } else {
                    setText("倒计时完成");
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
                if (isAutoShowText) {
                    updateText(millisUntilFinished);
                }
                if (countDownCallback != null) {
                    countDownCallback.onTick(TimerTextView.this,millisUntilFinished);
                }
            }
        });
        mCountDownTimer.start();
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(TimerTextView.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(TimerTextView.class.getName());
    }

    /**
     * 设置倒计时总时间
     * @param millisInFuture                    毫秒值
     */
    public void setMillisInFuture(long millisInFuture) {
        this.mMillisInFuture = millisInFuture;
    }


    /**
     * 设置倒计时间隔值
     * @param countdownInterval                 间隔，一般设置为1000毫秒。默认是1秒
     */
    public void setCountDownInterval(long countdownInterval) {
        this.mCountDownInterval = countdownInterval;
    }

    /**
     * 设置监听
     * @param callback                          callback
     */
    public void addCountDownCallback(CountDownCallback callback) {
        this.countDownCallback = callback;
    }

    /**
     * 是否展示文本内容，默认不展示。
     * 如果不展示，则用户需要自己添加callback监听，然后设置时间
     * 如果展示，用户不设置callback监听，显示默认设置时间
     * @param isAutoShowText                    是否展示
     */
    public void setAutoDisplayText(boolean isAutoShowText) {
        this.isAutoShowText = isAutoShowText;
    }

}
package com.ns.yc.lifehelper.ui.test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.receiver.TimerReceiver;
import com.ns.yc.lifehelper.service.TimerService;
import com.ns.yc.lifehelper.utils.DialogUtils;

import java.util.Calendar;

/**
 * Created by PC on 2017/10/26.
 * 作者：PC
 */

public class TestActivity extends AppCompatActivity {


    /**
     *
     *
     * person包，主要是探索装饰者模式简单案例实现
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */


//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        // 背景颜色
//        canvas.drawColor(Color.TRANSPARENT);
//        canvas.drawBitmap(mBitmapBall, 0, 0, mBitmapPaint);
//        if(mPaintType == TRACK_PAINT) {
//            canvas.drawBitmap(mBitmapTrajectory, 0, 0, mBitmapPaint);
//            canvas.drawPath(mPath, mTrackPaint);
//        }
//        else if(mPaintType == BALL_PAINT) {
//            canvas.drawPath(mPath, mBallPaint);
//        }
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        // 背景颜色
//        canvas.drawColor(Color.TRANSPARENT);
//
//        if(mPaintType == TRACK_PAINT) {
//            canvas.drawBitmap(mBitmapTrajectory, 0, 0, mBitmapPaint);
//            canvas.drawPath(mPath, mTrackPaint);
//        }
//        else if(mPaintType == BALL_PAINT) {
//            canvas.drawBitmap(mBitmapBall, 0, 0, mBitmapPaint);
//            canvas.drawPath(mPath, mBallPaint);
//        }
//    }

    private static TestResource mResource = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(mResource == null){
            mResource = new TestResource();
        }

        getSystemService(Context.ACCOUNT_SERVICE);

    }

    class TestResource {

    }

    /**---------------------------下面是测试功能，主要是做投资界全局弹窗功能--------------------------**/
    /**
     * 开启一个服务，在服务中倒计时
     */
    private void initService() {
        Intent intent = new Intent(this, TimerService.class);
        startService(intent);
    }

    /**
     * 倒计时
     */
    private void initTimer() {
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                //DialogUtils.showWindowToast("全局吐司");
                //IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity.
                //DialogUtils.showAllAlertDialog(GuideActivity.this);
                DialogUtils.showWindowDialog();
            }
        }.start();
    }


    /**
     * 在指定时长后执行某项操作
     */
    private void initAlarmManager() {
        //操作：发送一个广播，广播接收后Toast提示定时操作完成
        Intent intent = new Intent(this, TimerReceiver.class);
        intent.setAction("short");
        PendingIntent sender= PendingIntent.getBroadcast(this, 0, intent, 0);

        //设定一个n秒后的时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 30);

        AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        //或者以下面方式简化
        //alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5*1000, sender);
    }


    /**
     * 周期性的执行某项操作
     */
    private void initAlarmManagerSecond() {
        Intent intent =new Intent(this, TimerReceiver.class);

        intent.setAction("repeating");
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        //开始时间
        long firstTime = SystemClock.elapsedRealtime();

        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        //5秒一个周期，不停的发送广播
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 5*1000, sender);
    }


    void checkforTextView(View v) {
        if(v instanceof TextView) {
            //如果是TextView的控件会怎样怎样。。

        }else {
            //如果不是TextView的控件又该怎样
        }
    }

}




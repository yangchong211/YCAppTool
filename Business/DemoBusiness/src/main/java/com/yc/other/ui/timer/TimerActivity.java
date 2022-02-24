package com.yc.other.ui.timer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.yc.timerlib.timer.CountTimeTools;
import com.yc.other.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvTime;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private static final long MAX_TIME = 120000;
    private long curTime = 0;
    private static final int WHAT = 101;
    private boolean isPause = false;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT:
                    long sRecLen = (long) msg.obj;
                    //毫秒换成00:00:00格式的方式，自己写的。
                    mTvTime.setText(CountTimeTools.getCountTimeByLong(sRecLen));
                    if (sRecLen <= 0) {
                        mTimer.cancel();
                        curTime = 0;
                        Toast.makeText(TimerActivity.this, "结束", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        initView();
        initTimer();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyTimer();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    private void initView() {
        mTvTime = findViewById(R.id.tv_time);
        mTv1 = findViewById(R.id.tv_1);
        mTv2 = findViewById(R.id.tv_2);
        mTv3 = findViewById(R.id.tv_3);
        mTv4 = findViewById(R.id.tv_4);

        mTv1.setOnClickListener(this);
        mTv2.setOnClickListener(this);
        mTv3.setOnClickListener(this);
        mTv4.setOnClickListener(this);
    }

    private void initTimer() {
        if (mTimerTask==null){
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (curTime == 0) {
                        curTime = MAX_TIME;
                    } else {
                        curTime -= 1000;
                    }
                    Message message = new Message();
                    message.what = WHAT;
                    message.obj = curTime;
                    mHandler.sendMessage(message);
                }
            };
        }
        if (mTimer==null){
            mTimer = new Timer();
        }
    }

    public void destroyTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_1) {//开始
            if (!isPause) {
                destroyTimer();
                initTimer();
                // 参数：0，延时0秒后执行;1000，每隔1秒执行1次task。
                mTimer.schedule(mTimerTask, 0, 1000);
                isPause = false;
            }
        } else if (id == R.id.tv_2) {//结束销毁
            isPause = false;
            destroyTimer();
            mTvTime.setText(CountTimeTools.getCountTimeByLong(0));
        } else if (id == R.id.tv_3) {//暂停
            if (!isPause) {
                isPause = true;
                if (mTimer != null) {
                    mTimer.cancel();
                } else {
                    Toast.makeText(TimerActivity.this, "请先开始", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (id == R.id.tv_4) {//恢复暂停
            if (curTime != 0 && isPause) {
                destroyTimer();
                initTimer();
                mTimer.schedule(mTimerTask, 0, 1000);
                isPause = false;
            }
        }
    }
}

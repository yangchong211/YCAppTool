package com.yc.other.ui.timer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.other.R;
import androidx.annotation.Nullable;
import com.yc.timerlib.timer.CountDownTimer;
import com.yc.timerlib.timer.CountTimeTools;
import com.yc.timerlib.timer.TimerListener;

public class MyCountActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvTime;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;

    private static final long MAX_TIME = 6000;
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        initView();
        initCountDownTimer(MAX_TIME);
    }

    private void initCountDownTimer(long time) {
        mCountDownTimer = new CountDownTimer();
        //设置倒计时总时间
        mCountDownTimer.setMillisInFuture(MAX_TIME);
        //设置倒计时间隔值
        mCountDownTimer.setCountdownInterval(1000);
        //设置倒计时监听
        mCountDownTimer.setCountDownListener(new TimerListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
                mTvTime.setText("完成!");
            }

            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("-------onTick-------","----"+millisUntilFinished);
                mTvTime.setText(CountTimeTools.getCountTimeByLong(millisUntilFinished));
                /*for (int i=0 ; i<10000000; i++){
                    String s = i + "";
                }*/
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer!=null){
            mCountDownTimer.cancel();
            mCountDownTimer = null;
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_1) {//开始
            mCountDownTimer.start();
        } else if (id == R.id.tv_2) {//结束销毁
            mCountDownTimer.cancel();
        } else if (id == R.id.tv_3) {//暂停
            mCountDownTimer.pause();
        } else if (id == R.id.tv_4) {//恢复暂停
            mCountDownTimer.resume();
        }
    }
}

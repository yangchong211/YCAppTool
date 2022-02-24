package com.yc.other.ui.timer;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.other.R;
import androidx.annotation.Nullable;

public class ChronometerActivity extends AppCompatActivity implements View.OnClickListener {

    Chronometer mChronometer;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;


    private static final long MAX_TIME = 120000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);

        initView();
        initCountDownTimer(MAX_TIME);
    }

    private void initCountDownTimer(long time) {
        mChronometer.setBase(SystemClock.elapsedRealtime() + MAX_TIME);
        //这个方法 在 sdk -24 才可以使用，可以来说非常不适用了
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mChronometer.setCountDown(true);
        }
        mChronometer.start();
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                long second = time / 1000;
                if (second == 0) {
                    chronometer.stop();
                }
                Log.e("TAG", "相差时间：" + time);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        mChronometer = findViewById(R.id.chronometer);
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
            mChronometer.setBase(SystemClock.elapsedRealtime() + MAX_TIME);
            mChronometer.start();
        } else if (id == R.id.tv_2) {//结束销毁
            mChronometer.stop();
        } else if (id == R.id.tv_3) {//暂停
        } else if (id == R.id.tv_4) {//恢复暂停
        }
    }
}

package com.yc.other.ui.timer;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.other.R;
import androidx.annotation.Nullable;
import com.yc.timerlib.timer.CountTimeTools;

public class HandlerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvTime;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;

    private static final long MAX_TIME = 120000;
    private long curTime = 0;
    private boolean isPause = false;

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            curTime -=1000;
            mTvTime.setText(CountTimeTools.getCountTimeByLong(curTime));
            if (curTime > 0) {
                mHandler.postDelayed(this, 1000);
            } else {
                Toast.makeText(HandlerActivity.this,"运行结束",Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_1) {//开始
            curTime = MAX_TIME;
            mHandler.removeCallbacks(runnable);
            mHandler.postDelayed(runnable, 1000);
            isPause = false;
        } else if (id == R.id.tv_2) {//结束销毁
            mHandler.removeCallbacks(runnable);
            mTvTime.setText(CountTimeTools.getCountTimeByLong(0));
        } else if (id == R.id.tv_3) {//暂停
            if (!isPause) {
                mHandler.removeCallbacks(runnable);
                isPause = true;
            }
        } else if (id == R.id.tv_4) {//恢复暂停
            if (curTime != 0 && isPause) {
                mHandler.removeCallbacks(runnable);
                mHandler.postDelayed(runnable, 1000);
                isPause = false;
            }
        }
    }
}

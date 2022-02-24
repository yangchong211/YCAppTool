package com.yc.other.ui.timer;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yc.timerlib.timer.CountTimeTools;
import com.yc.other.R;

public class CountActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvTime;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;

    final String TAG = "CountDownTimer";

    private static final long MAX_TIME = 5000;
    private long curTime = 0;
    private boolean isPause = false;
    private CountDownTimer mCountDownTimer;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        initView();
        initCountDownTimer(MAX_TIME);
    }

    private void initCountDownTimer(long time) {
        mCountDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "----倒计时----onTick--"+millisUntilFinished);
                CountActivity.this.curTime = millisUntilFinished;
                mTvTime.setText(CountTimeTools.getCountTimeByLong(millisUntilFinished));
                isPause = false;
            }

            public void onFinish() {
                mTvTime.setText("完成!");
                Log.i(TAG, "----倒计时----onFinish");
            }
        };
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
            if (!isPause) {
                isPause = false;
                mCountDownTimer.start();
            }
        } else if (id == R.id.tv_2) {//结束销毁
            isPause = false;
            mCountDownTimer.cancel();
            mTvTime.setText(CountTimeTools.getCountTimeByLong(0));
        } else if (id == R.id.tv_3) {//暂停
            if (!isPause) {
                isPause = true;
                mCountDownTimer.cancel();
            }
        } else if (id == R.id.tv_4) {//恢复暂停
            if (curTime != 0 && isPause) {
                //将上次当前剩余时间作为新的时长
                //将上次当前剩余时间作为新的时长
                initCountDownTimer(curTime);
                mCountDownTimer.start();
                isPause = false;
            }
        }
    }
}

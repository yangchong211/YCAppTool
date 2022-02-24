package com.yc.other.ui.timer;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.other.R;
import androidx.annotation.Nullable;
import com.yc.timerlib.view.TimerTextView;

public class CountViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TimerTextView mTvTime;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;


    private static final long MAX_TIME = 120000;
    private long curTime = 0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_view);

        initView();
        mTvTime.setMillisInFuture(MAX_TIME);
        mTvTime.setCountDownInterval(1000);
        mTvTime.setAutoDisplayText(true);
    }




    private void initView() {
        mTvTime = findViewById(R.id.tv_count);
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
            mTvTime.start();
        } else if (id == R.id.tv_2) {//结束销毁
            mTvTime.cancel();
        } else if (id == R.id.tv_3) {//暂停
            mTvTime.pause();
        } else if (id == R.id.tv_4) {//恢复暂停
            mTvTime.resume();
        }
    }
}

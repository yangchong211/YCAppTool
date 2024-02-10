package com.yc.other.ui.timer;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;


import com.yc.looperthread.heart.LoopRequestService;
import com.yc.other.R;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/9/11
 *     desc  : 倒计时器案例
 *     revise:
 * </pre>
 */
public class TimerMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_main);

        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
        findViewById(R.id.tv_5).setOnClickListener(this);
        findViewById(R.id.tv_6).setOnClickListener(this);
        findViewById(R.id.tv_7).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_1) {
            startActivity(new Intent(this, HandlerActivity.class));
        } else if (id == R.id.tv_2) {
            startActivity(new Intent(this, CountActivity.class));
        } else if (id == R.id.tv_3) {
            startActivity(new Intent(this, TimerActivity.class));
        } else if (id == R.id.tv_4) {
            startActivity(new Intent(this, ChronometerActivity.class));
        } else if (id == R.id.tv_5) {
            startActivity(new Intent(this, MyCountActivity.class));
        } else if (id == R.id.tv_6) {
            startActivity(new Intent(this, CountViewActivity.class));
        } else if (id == R.id.tv_7) {
            LoopRequestService.startLoopService(this);
        }
    }
}

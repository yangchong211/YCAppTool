package com.yc.appmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.yc.library.base.mvp.BaseActivity;
import com.yc.monitorphone.MonitorPhoneActivity;
import com.yc.monitorpinglib.MonitorPingActivity;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.toolutils.click.PerfectClickListener;

public class MonitorMainActivity extends BaseActivity {

    private RoundTextView tvPhoneMonitor;
    private RoundTextView tvPingMonitor;

    @Override
    public int getContentView() {
        return R.layout.activity_monitor_main;
    }

    @Override
    public void initView() {
        tvPhoneMonitor = findViewById(R.id.tv_phone_monitor);
        tvPingMonitor = findViewById(R.id.tv_ping_monitor);
    }

    @Override
    public void initListener() {
        tvPhoneMonitor.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                MonitorPhoneActivity.startActivity(MonitorMainActivity.this);
            }
        });
        tvPingMonitor.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                MonitorPingActivity.startActivity(MonitorMainActivity.this
                        ,"https://www.wanandroid.com/banner/json");
            }
        });
    }

    @Override
    public void initData() {

    }

}
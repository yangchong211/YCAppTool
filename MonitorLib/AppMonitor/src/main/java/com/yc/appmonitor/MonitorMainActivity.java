package com.yc.appmonitor;


import android.content.Intent;
import android.view.View;

import com.yc.appmonitor.apm.ApmTestActivity;
import com.yc.appmonitor.crash.CrashTestActivity;
import com.yc.appmonitor.net.NetworkActivity;
import com.yc.toolutils.click.PerfectClickListener;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.monitorfilelib.FileExplorerActivity;
import com.yc.monitorphone.MonitorPhoneActivity;
import com.yc.monitorpinglib.MonitorPingActivity;
import com.yc.monitorspeed.ConnectionActivity;
import com.yc.monitortimelib.monitor.MonitorTimeHelper;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.crash.lib.CrashListActivity;

public class MonitorMainActivity extends BaseActivity {

    private RoundTextView tvPhoneMonitor;
    private RoundTextView tvPingMonitor;
    private RoundTextView tvFileMonitor;
    private RoundTextView tvCrashMonitor;
    private RoundTextView tvCrashTest;
    private RoundTextView tvNetSpeed;
    private RoundTextView tvApmTest;
    private RoundTextView tvNetHttp;
    private RoundTextView tvFps;
    private int count = 0 ;
    @Override
    public int getContentView() {
        return R.layout.activity_monitor_main;
    }

    @Override
    public void initView() {
        tvPhoneMonitor = findViewById(R.id.tv_phone_monitor);
        tvPingMonitor = findViewById(R.id.tv_ping_monitor);
        tvFileMonitor = findViewById(R.id.tv_file_monitor);
        tvCrashMonitor = findViewById(R.id.tv_crash_monitor);
        tvCrashTest = findViewById(R.id.tv_crash_test);
        tvNetSpeed = findViewById(R.id.tv_net_speed);
        tvApmTest = findViewById(R.id.tv_apm_test);
        tvNetHttp = findViewById(R.id.tv_net_http);
        tvFps = findViewById(R.id.tv_fps);
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
        tvFileMonitor.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                FileExplorerActivity.startActivity(MonitorMainActivity.this);
            }
        });
        tvCrashMonitor.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                CrashListActivity.startActivity(MonitorMainActivity.this);
            }
        });
        tvCrashTest.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                MonitorTimeHelper.start("startActivity统计耗时");
                Intent intent = new Intent(MonitorMainActivity.this, CrashTestActivity.class);
                startActivity(intent);
            }
        });
        tvNetSpeed.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                MonitorTimeHelper.start("Main Click");
                ConnectionActivity.startActivity(MonitorMainActivity.this);
            }
        });
        tvApmTest.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intent = new Intent(MonitorMainActivity.this, ApmTestActivity.class);
                startActivity(intent);
            }
        });
        tvNetHttp.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                NetworkActivity.start(MonitorMainActivity.this);
            }
        });
        tvFps.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

            }
        });
    }

    @Override
    public void initData() {

    }


    @Override
    protected void onStop() {
        super.onStop();
        MonitorTimeHelper.end("Main Click");
    }


}
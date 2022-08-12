package com.yc.appserver;

import android.view.View;

import com.yc.appserver.log.LogTestActivity;
import com.yc.appserver.priva.PrivateTestActivity;
import com.yc.appserver.vibrator.VibratorTestActivity;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.toolutils.click.PerfectClickListener;

public class ServerMainActivity extends BaseActivity {

    private RoundTextView tvLogServer;
    private RoundTextView tvVibratorTest;
    private RoundTextView tvPrivateTest;

    @Override
    public int getContentView() {
        return R.layout.activity_server_main;
    }

    @Override
    public void initView() {
        tvLogServer = findViewById(R.id.tv_log_server);
        tvVibratorTest = findViewById(R.id.tv_vibrator_test);
        tvPrivateTest = findViewById(R.id.tv_private_test);
    }

    @Override
    public void initListener() {
        tvLogServer.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                LogTestActivity.Companion.startActivity(ServerMainActivity.this);
            }
        });
        tvVibratorTest.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                VibratorTestActivity.Companion.startActivity(ServerMainActivity.this);
            }
        });
        tvPrivateTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivateTestActivity.Companion.startActivity(ServerMainActivity.this);
            }
        });
    }

    @Override
    public void initData() {

    }
}
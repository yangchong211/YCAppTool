package com.yc.appserver;

import android.view.View;

import com.yc.appserver.http.NetMainActivity;
import com.yc.appserver.log.LogTestActivity;
import com.yc.appserver.priva.PrivateTestActivity;
import com.yc.appserver.thread.HighCpuActivity;
import com.yc.appserver.vibrator.VibratorTestActivity;
import com.yc.blesample.ble.BleMainActivity;
import com.yc.blesample.chat.base.ChatMainActivity;
import com.yc.blesample.demo.BleEasyActivity;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.logclient.LogUtils;
import com.yc.monitorfilelib.FileExplorerActivity;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.toolutils.click.PerfectClickListener;

public class ServerMainActivity extends BaseActivity {

    private RoundTextView tvLogServer;
    private RoundTextView tvVibratorTest;
    private RoundTextView tvPrivateTest;
    private RoundTextView tvNetTest;
    private RoundTextView tvThreadTest;
    private RoundTextView tvFileTest;
    private RoundTextView tvBle1;
    private RoundTextView tvBle2;
    private RoundTextView tvBle3;

    @Override
    public int getContentView() {
        LogUtils.i("init getContentView");
        return R.layout.activity_server_main;
    }

    @Override
    public void initView() {
        tvLogServer = findViewById(R.id.tv_log_server);
        tvVibratorTest = findViewById(R.id.tv_vibrator_test);
        tvPrivateTest = findViewById(R.id.tv_private_test);
        tvNetTest = findViewById(R.id.tv_net_test);
        tvThreadTest = findViewById(R.id.tv_thread_test);
        tvFileTest = findViewById(R.id.tv_file_test);
        tvBle1 = findViewById(R.id.tv_ble_1);
        tvBle2 = findViewById(R.id.tv_ble_2);
        tvBle3 = findViewById(R.id.tv_ble_3);
        LogUtils.i("init view");
    }

    @Override
    public void initListener() {
        tvLogServer.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                LogUtils.i("click tvLogServer");
                LogTestActivity.Companion.startActivity(ServerMainActivity.this);
            }
        });
        tvVibratorTest.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                LogUtils.i("click tvVibratorTest");
                VibratorTestActivity.Companion.startActivity(ServerMainActivity.this);
            }
        });
        tvPrivateTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("click tvPrivateTest");
                PrivateTestActivity.Companion.startActivity(ServerMainActivity.this);
            }
        });
        tvNetTest.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                LogUtils.i("click tvNetTest");
                NetMainActivity.startActivity(ServerMainActivity.this);
            }
        });
        tvThreadTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("click tvThreadTest");
                HighCpuActivity.testHighCpu();
            }
        });
        tvFileTest.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                FileExplorerActivity.startActivity(ServerMainActivity.this);
            }
        });
        tvBle1.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                BleEasyActivity.startActivity(ServerMainActivity.this);
            }
        });
        tvBle2.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                ChatMainActivity.startActivity(ServerMainActivity.this);
            }
        });
        tvBle3.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                BleMainActivity.startActivity(ServerMainActivity.this);
            }
        });
    }

    @Override
    public void initData() {
        LogUtils.i("initData");
    }
}
package com.yc.other.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.other.R;
import com.yc.toolutils.sntp.NtpGetTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NtpTimeActivity extends AppCompatActivity {

    private TextView tvTimePst;
    private TextView tvTimeGmt;
    private TextView tvTimeDevice;

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, NtpTimeActivity.class);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npt_time);
        tvTimePst = findViewById(R.id.tv_time_pst);
        tvTimeGmt = findViewById(R.id.tv_time_gmt);
        tvTimeDevice = findViewById(R.id.tv_time_device);
        Button btnRefresh = findViewById(R.id.btn_refresh);
        btnRefresh.setEnabled(NtpGetTime.isInitialized());
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnRefresh();
            }
        });
    }

    public void onBtnRefresh() {
        if (!NtpGetTime.isInitialized()) {
            Toast.makeText(this, "Sorry TrueTime not yet initialized. Trying again.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "刷新时间", Toast.LENGTH_SHORT).show();
        //获取校对后时间
        Date trueTime = NtpGetTime.now();
        //获取当前设备时间
        Date deviceTime = new Date();

        tvTimeGmt.setText(getString(R.string.tt_time_gmt,
                                  _formatDate(trueTime, "yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT+8"))));
        tvTimePst.setText(getString(R.string.tt_time_pst,
                                  _formatDate(trueTime, "yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT+8"))));
        tvTimeDevice.setText(getString(R.string.tt_time_device,
                                         _formatDate(deviceTime, "yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT+8"))));
    }

    private String _formatDate(Date date, String pattern, TimeZone timeZone) {
        DateFormat format = new SimpleDateFormat(pattern, Locale.CHINESE);
        format.setTimeZone(timeZone);
        return format.format(date);
    }

}

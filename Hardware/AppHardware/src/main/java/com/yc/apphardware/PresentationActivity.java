package com.yc.apphardware;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.toolutils.click.PerfectClickListener;
import com.yc.sceondary.DisplayManager;



public class PresentationActivity extends AppCompatActivity {

    private TextView tvView1;
    private TextView tvView2;
    private TextView tvView3;
    private TextView tvView4;

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, PresentationActivity.class);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        tvView1 = findViewById(R.id.tv_view_1);
        tvView2 = findViewById(R.id.tv_view_2);
        tvView3 = findViewById(R.id.tv_view_3);
        tvView4 = findViewById(R.id.tv_view_4);
        permission();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisplayManager.getInstance().dismissDisplay(this);
    }

    private void permission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            }
        }
    }

    private void initListener() {
        tvView1.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                DisplayManager.getInstance().showDisplay(PresentationActivity.this,R.layout.activity_presentation);
            }
        });
        tvView2.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                DisplayManager.getInstance().showDisplay(PresentationActivity.this,R.layout.activity_presentation);
            }
        });
        tvView3.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                DisplayManager.getInstance().showDisplay(PresentationActivity.this,R.layout.activity_presentation);
            }
        });
        tvView4.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
            }
        });
    }
}

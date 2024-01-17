package com.yc.widgetbusiness.screen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.sceondary.present.PresentationImpl;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.toolutils.click.PerfectClickListener;
import com.yc.widgetbusiness.R;


public class PresentationActivity extends AppCompatActivity {

    private TextView tvView1;
    private TextView tvView2;
    private TextView tvView3;

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
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        tvView1 = findViewById(R.id.tv_view_1);
        tvView2 = findViewById(R.id.tv_view_2);
        tvView3 = findViewById(R.id.tv_view_3);
        initListener();
    }

    private void initListener() {
        tvView1.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                PresentationImpl presentation = new PresentationImpl();
                presentation.showView(R.layout.activity_presentation);
            }
        });
        tvView2.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                PresentationImpl presentation = new PresentationImpl();
                presentation.showView(R.layout.activity_red_view);
            }
        });
        tvView3.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                PresentationImpl presentation = new PresentationImpl();
                presentation.showView(R.layout.activity_event_view);
            }
        });
    }
}

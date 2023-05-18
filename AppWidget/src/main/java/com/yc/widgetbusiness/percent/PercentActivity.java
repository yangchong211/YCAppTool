package com.yc.widgetbusiness.percent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.statusbar.bar.StateAppBar;
import com.yc.widgetbusiness.R;


public class PercentActivity extends AppCompatActivity {

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, PercentActivity.class);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percent_view);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
    }
}

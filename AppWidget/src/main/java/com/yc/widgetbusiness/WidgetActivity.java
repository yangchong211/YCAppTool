package com.yc.widgetbusiness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.yc.baseclasslib.activity.BaseAppActivity;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.toolutils.click.PerfectClickListener;

public class WidgetActivity extends BaseActivity {

    private RoundTextView tvWidgetRound;
    private RoundTextView tvWidgetImage;

    @Override
    public int getContentView() {
        return R.layout.activity_widget_main;
    }

    @Override
    public void initView() {
        tvWidgetRound = findViewById(R.id.tv_widget_round);
        tvWidgetImage = findViewById(R.id.tv_widget_image);
    }

    @Override
    public void initListener() {
        tvWidgetRound.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                RoundCornersActivity.startActivity(WidgetActivity.this);
            }
        });
        tvWidgetImage.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                ImageViewActivity.startActivity(WidgetActivity.this);
            }
        });
    }

    @Override
    public void initData() {

    }


}

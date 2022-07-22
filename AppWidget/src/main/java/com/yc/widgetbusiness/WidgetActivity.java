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

    @Override
    public int getContentView() {
        return R.layout.activity_widget_main;
    }

    @Override
    public void initView() {
        tvWidgetRound = findViewById(R.id.tv_widget_round);
    }

    @Override
    public void initListener() {
        tvWidgetRound.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                RoundCornersActivity.startActivity(WidgetActivity.this);
            }
        });
    }

    @Override
    public void initData() {

    }


}

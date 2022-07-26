package com.yc.widgetbusiness;

import android.view.View;

import com.yc.library.base.mvp.BaseActivity;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.toolutils.click.PerfectClickListener;
import com.yc.widgetbusiness.chart.PreActivity;
import com.yc.widgetbusiness.image.ImageViewActivity;
import com.yc.widgetbusiness.round.RoundCornersActivity;
import com.yc.widgetbusiness.textview.MixtureTextViewActivity;

public class WidgetActivity extends BaseActivity {

    private RoundTextView tvWidgetRound;
    private RoundTextView tvWidgetImage;
    private RoundTextView tvWidgetSpan;
    private RoundTextView tvWidgetChart;

    @Override
    public int getContentView() {
        return R.layout.activity_widget_main;
    }

    @Override
    public void initView() {
        tvWidgetRound = findViewById(R.id.tv_widget_round);
        tvWidgetImage = findViewById(R.id.tv_widget_image);
        tvWidgetSpan = findViewById(R.id.tv_widget_span);
        tvWidgetChart = findViewById(R.id.tv_widget_chart);
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
        tvWidgetSpan.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                MixtureTextViewActivity.startActivity(WidgetActivity.this);
            }
        });
        tvWidgetChart.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                PreActivity.Companion.startActivity(WidgetActivity.this);
            }
        });
    }

    @Override
    public void initData() {

    }


}

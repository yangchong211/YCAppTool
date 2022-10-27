package com.yc.widgetbusiness;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.roundcorner.view.RoundTextView;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.toolutils.click.PerfectClickListener;
import com.yc.widgetbusiness.banner.BannerActivity;
import com.yc.widgetbusiness.blur.BlurActivity;
import com.yc.widgetbusiness.chart.PreActivity;
import com.yc.widgetbusiness.floatpage.FloatActivity;
import com.yc.widgetbusiness.image.ImageViewActivity;
import com.yc.widgetbusiness.red.RedViewActivity;
import com.yc.widgetbusiness.round.RoundCornersActivity;
import com.yc.widgetbusiness.shadow.ShadowActivity;
import com.yc.widgetbusiness.textview.MixtureTextViewActivity;
import com.yc.widgetbusiness.zoom.ZoomMainActivity;

public class WidgetActivity extends AppCompatActivity {

    private RoundTextView tvWidgetRound;
    private RoundTextView tvWidgetImage;
    private RoundTextView tvWidgetSpan;
    private RoundTextView tvWidgetChart;
    private RoundTextView tvWidgetShadow;
    private RoundTextView tvWidgetZoom;
    private RoundTextView tvWidgetRed;
    private RoundTextView tvWidgetFloat;
    private RoundTextView tvWidgetBlur;
    private RoundTextView tvWidgetBanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_main);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        initView();
        initListener();
    }

    private void initView() {
        tvWidgetRound = findViewById(R.id.tv_widget_round);
        tvWidgetImage = findViewById(R.id.tv_widget_image);
        tvWidgetSpan = findViewById(R.id.tv_widget_span);
        tvWidgetChart = findViewById(R.id.tv_widget_chart);
        tvWidgetShadow = findViewById(R.id.tv_widget_shadow);
        tvWidgetZoom = findViewById(R.id.tv_widget_zoom);
        tvWidgetRed = findViewById(R.id.tv_widget_red);
        tvWidgetFloat = findViewById(R.id.tv_widget_float);
        tvWidgetBlur = findViewById(R.id.tv_widget_blur);
        tvWidgetBanner = findViewById(R.id.tv_widget_banner);
    }

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
        tvWidgetShadow.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                ShadowActivity.startActivity(WidgetActivity.this);
            }
        });
        tvWidgetZoom.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                startActivity(new Intent(WidgetActivity.this,ZoomMainActivity.class));
            }
        });
        tvWidgetRed.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                startActivity(new Intent(WidgetActivity.this, RedViewActivity.class));
            }
        });
        tvWidgetFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WidgetActivity.this, FloatActivity.class));
            }
        });
        tvWidgetBlur.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                startActivity(new Intent(WidgetActivity.this, BlurActivity.class));
            }
        });
        tvWidgetBanner.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                startActivity(new Intent(WidgetActivity.this, BannerActivity.class));
            }
        });
    }

}

package com.ns.yc.lifehelper.ui.other.weather;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.other.weather.view.WeatherFragment;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：天气页面
 * 修订历史：
 * ================================================
 */
public class WeatherActivity extends BaseActivity {

    @Bind(R.id.tv_publish_time)
    public TextView tvPublishTime;
    @Bind(R.id.tv_temp)
    public TextView tvTemp;
    @Bind(R.id.tv_weather)
    public TextView tvWeather;
    @Bind(R.id.weather_icon_image_view)
    public ImageView weatherIconImageView;
    @Bind(R.id.rl_backdrop)
    public RelativeLayout rlBackdrop;
    @Bind(R.id.tool_bar)
    public Toolbar toolBar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.app_bar)
    public AppBarLayout appBar;
    @Bind(R.id.fragment_container)
    public FrameLayout fragmentContainer;

    @Override
    public int getContentView() {
        return R.layout.activity_weather_main;
    }

    @Override
    public void initView() {
        initToolBar();
        initFragment();
    }

    private void initToolBar() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolBar.setNavigationIcon(R.drawable.ic_back_white);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        WeatherFragment weatherFragment = new WeatherFragment();
        //ft.replace(R.id.fragment_container, weatherFragment,null);
        ft.add(R.id.fragment_container,weatherFragment);
        ft.commit();
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

}

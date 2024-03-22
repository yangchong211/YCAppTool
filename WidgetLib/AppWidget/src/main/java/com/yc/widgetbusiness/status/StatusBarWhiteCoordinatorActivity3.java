package com.yc.widgetbusiness.status;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.yc.statusbar.bar.StateAppBar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.ycbjie.ycstatusbar.R;


public class StatusBarWhiteCoordinatorActivity3 extends AppCompatActivity {

    private AppBarLayout mAppBarLayout;

    /**
     * 配合CoordinatorLayout使用
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATES{
        int EXPANDED = 3;
        int COLLAPSED = 2;
        int INTERMEDIATE = 1;
    }
    private int state;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_white_coordinator3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_layout);

        StateAppBar.setStatusBarLightForCollapsingToolbar(this,
                mAppBarLayout, collapsingToolbarLayout, toolbar, Color.WHITE);



        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                if (verticalOffset == 0) {
                    if (state != STATES.EXPANDED) {
                        //修改状态标记为展开
                        state = STATES.EXPANDED;
                        Log.e("AppBarLayout","修改状态标记为展开");
                    }
                } else if (Math.abs(verticalOffset) >= totalScrollRange) {
                    if (state != STATES.COLLAPSED) {
                        //修改状态标记为折叠
                        state = STATES.COLLAPSED;
                        Log.e("AppBarLayout","修改状态标记为折叠");
                    }
                } else {
                    if (state != STATES.INTERMEDIATE) {
                        //修改状态标记为中间
                        state = STATES.INTERMEDIATE;
                        //代码设置是否拦截事件
                        Log.e("AppBarLayout","修改状态标记为中间");
                    }
                    int absVerticalOffset = Math.abs(verticalOffset);
                    int startOffset = absVerticalOffset;
                    int height = totalScrollRange - absVerticalOffset;
                    //正常 240*240   top  300
                    //缩放到最小 80*80       top   60
                    float scale = height/(totalScrollRange*1.0f);
                    Log.e("AppBarLayout","比例大小"+scale);
                    //指的是手指滑动
                    if (startOffset > Math.abs(verticalOffset)){
                        //从下到上滑动
                    }else {
                        //从上到下滑动
                    }
                }
            }
        });

    }
}

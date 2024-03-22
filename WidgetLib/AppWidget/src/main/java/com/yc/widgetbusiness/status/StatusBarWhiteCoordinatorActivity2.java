package com.yc.widgetbusiness.status;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.yc.statusbar.bar.StateAppBar;

import cn.ycbjie.ycstatusbar.R;


public class StatusBarWhiteCoordinatorActivity2 extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_white_coordinator2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_layout);

        StateAppBar.setStatusBarLightForCollapsingToolbar(this,
                mAppBarLayout, collapsingToolbarLayout, toolbar, Color.WHITE);

    }
}

package com.ycbjie.other.ui.activity;

import android.content.Intent;
import android.view.View;

import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.other.R;

public class TestActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getContentView() {
        return R.layout.activity_test_other;
    }

    @Override
    public void initView() {
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
        findViewById(R.id.tv_5).setOnClickListener(this);
        findViewById(R.id.tv_6).setOnClickListener(this);
        findViewById(R.id.tv_7).setOnClickListener(this);
        findViewById(R.id.tv_8).setOnClickListener(this);
        findViewById(R.id.tv_9).setOnClickListener(this);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_1) {
            startActivity(new Intent(this, MeBannerActivity.class));
        } else if (i == R.id.tv_2) {
            startActivity(new Intent(this, BannerViewActivity.class));
        } else if (i == R.id.tv_3) {
            startActivity(new Intent(this, ImageGalleryActivity.class));
        } else if (i == R.id.tv_4) {
            startActivity(new Intent(this, ProgressThirdActivity.class));
        } else if (i == R.id.tv_5) {
            startActivity(new Intent(this, ZoomLargeImageActivity.class));
        } else if (i == R.id.tv_6){
            startActivity(new Intent(this, TestFirstActivity.class));
        } else if (i == R.id.tv_7){
            startActivity(new Intent(this, ScrollActivity.class));
        } else if (i == R.id.tv_8){
            startActivity(new Intent(this, MixtureTextViewActivity.class));
        }else if (i == R.id.tv_9){
            startActivity(new Intent(this, CloneAbleActivity.class));
        }
    }




}

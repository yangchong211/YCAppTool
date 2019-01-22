package com.ycbjie.book.ui.activity;

import android.content.Intent;
import android.view.View;

import com.ycbjie.book.R;
import com.ycbjie.library.base.mvp.BaseActivity;

public class GameActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getContentView() {
        return R.layout.activity_game;
    }

    @Override
    public void initView() {
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
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
            startActivity(new Intent(this, PinTuGameActivity.class));
        } else if (i == R.id.tv_2) {
            startActivity(new Intent(this, AirGameActivity.class));

        }
    }
}

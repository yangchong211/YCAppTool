package com.ycbjie.note.ui.activity;

import android.content.Intent;

import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.note.R;


public class NoteTestActivity extends BaseActivity {
    @Override
    public int getContentView() {
        return R.layout.activity_note_test;
    }

    @Override
    public void initView() {
        findViewById(R.id.tv_1).setOnClickListener(v -> {
            //跳转markdown
            startActivity(new Intent(NoteTestActivity.this,MdMainActivity.class));
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}

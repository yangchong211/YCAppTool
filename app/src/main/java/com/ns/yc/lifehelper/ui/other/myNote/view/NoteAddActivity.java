package com.ns.yc.lifehelper.ui.other.myNote.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp.BaseActivity;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/12
 * 描    述：我的笔记本添加页面
 * 修订历史：
 * ================================================
 */
public class NoteAddActivity extends BaseActivity {


    @Bind(R.id.fragment)
    FrameLayout fragment;
    private NoteAddFragment noteAddFragment;
    protected String from;          //1为新建；2为更新
    protected String id;
    protected String time;
    protected String content;

    @Override
    public int getContentView() {
        return R.layout.activity_note_add;
    }

    @Override
    public void initView() {
        initFragment();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        initIntentData();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        id = intent.getStringExtra("id");
        time = intent.getStringExtra("time");
        content = intent.getStringExtra("content");
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        noteAddFragment = new  NoteAddFragment();
        ft.replace(R.id.fragment, noteAddFragment,null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if(!noteAddFragment.onBackPressed()){
            super.onBackPressed();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }


}

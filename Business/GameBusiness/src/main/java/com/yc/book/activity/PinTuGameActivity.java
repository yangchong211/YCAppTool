package com.yc.book.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;



import com.yc.library.base.mvp.BaseActivity;
import com.ycbjie.book.R;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/9/11
 *     desc  : 拼美女图游戏
 *     revise:
 * </pre>
 */
public class PinTuGameActivity extends BaseActivity {

    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private Toolbar toolbar;

    @Override
    public int getContentView() {
        return R.layout.activity_pin_tu_game;
    }

    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("妹子智慧拼图");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }



    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(v -> finish());
    }

    @Override
    public void initData() {

    }
}

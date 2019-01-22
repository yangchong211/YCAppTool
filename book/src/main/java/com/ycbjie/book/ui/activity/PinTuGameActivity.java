package com.ycbjie.book.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.ycbjie.book.R;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/9/11
 *     desc  : 拼美女图游戏
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_OTHER_PIN_TU_ACTIVITY)
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

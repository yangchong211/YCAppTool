package com.ns.yc.lifehelper.ui.other.timer;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.weight.SwitchButton;
import com.ns.yc.lifehelper.ui.weight.WatcherBoard;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/22
 * 描    述：秒表计时器
 * 修订历史：
 * ================================================
 */
public class TimerActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.watch)
    WatcherBoard watch;
    @Bind(R.id.switch_night)
    SwitchButton switchNight;
    @Bind(R.id.tv_set_time)
    TextView tvSetTime;
    @Bind(R.id.rl_set_time)
    RelativeLayout rlSetTime;
    @Bind(R.id.tv_timer_sound)
    TextView tvTimerSound;
    @Bind(R.id.rl_timer_sound)
    RelativeLayout rlTimerSound;
    @Bind(R.id.tv_exit)
    TextView tvExit;
    @Bind(R.id.tv_set_re)
    TextView tvSetRe;
    @Bind(R.id.rl_set_re)
    RelativeLayout rlSetRe;
    private TimerActivity activity;

    @Override
    public int getContentView() {
        return R.layout.activity_timer_main;
    }

    @Override
    public void initView() {
        activity = TimerActivity.this;
        initToolBar();
    }

    private void initToolBar() {
        toolbarTitle.setText("计时器");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        rlSetTime.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.rl_set_time:
                showTimerPickerDialog();
                break;
        }
    }

    private void showTimerPickerDialog() {

    }

}

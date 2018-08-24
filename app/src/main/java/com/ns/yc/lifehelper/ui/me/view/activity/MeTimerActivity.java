package com.ns.yc.lifehelper.ui.me.view.activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.weight.WatcherBoard;
import com.ns.yc.ycutilslib.switchButton.SwitchButton;

import butterknife.BindView;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : 秒表计时器
 *     revise: v1.4 17年6月8日
 *             v1.5 17年10月3日修改
 * </pre>
 */
public class MeTimerActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.watch)
    WatcherBoard watch;
    @BindView(R.id.switch_night)
    SwitchButton switchNight;
    @BindView(R.id.tv_set_time)
    TextView tvSetTime;
    @BindView(R.id.rl_set_time)
    RelativeLayout rlSetTime;
    @BindView(R.id.tv_timer_sound)
    TextView tvTimerSound;
    @BindView(R.id.rl_timer_sound)
    RelativeLayout rlTimerSound;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.tv_set_re)
    TextView tvSetRe;
    @BindView(R.id.rl_set_re)
    RelativeLayout rlSetRe;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_me_timer;
    }

    @Override
    public void initView() {
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
            default:
                break;
        }
    }

    private void showTimerPickerDialog() {

    }

}

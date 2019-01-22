package com.ns.yc.lifehelper.ui.me.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.ns.yc.lifehelper.R;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ns.yc.lifehelper.weight.WatcherBoard;
import com.ns.yc.ycutilslib.switchButton.SwitchButton;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;


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

    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private WatcherBoard watch;
    private SwitchButton switchNight;
    private TextView tvSetTime;
    private RelativeLayout rlSetTime;
    private TextView tvTimerSound;
    private RelativeLayout rlTimerSound;
    private TextView tvExit;
    private TextView tvSetRe;
    private RelativeLayout rlSetRe;


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
        initFindById();
        initToolBar();
    }

    private void initFindById() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        rlSetTime = findViewById(R.id.rl_set_time);
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
                //跨模块实现ForResult返回数据，跳转逻辑
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_FEEDBACK,
                        null,this,666);
                break;
            default:
                break;
        }
    }


    //跨模块实现ForResult返回数据，接收数据数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 666:
                if (data != null) {
                    String content = data.getStringExtra(Constant.CONTENT);
                    LogUtils.e("返回的内容"+content);
                }
                break;
            default:
                break;
        }

    }
}

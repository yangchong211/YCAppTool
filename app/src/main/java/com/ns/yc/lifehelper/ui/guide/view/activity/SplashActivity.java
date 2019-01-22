package com.ns.yc.lifehelper.ui.guide.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.yccountdownviewlib.CountDownView;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.constant.Constant;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/01/21
 *     desc  : 启动页面
 *     revise: 针对启动页闪屏优化处理，可以看我这篇博客：https://www.jianshu.com/p/4dcc8e0e6966
 * </pre>
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //将window的背景图设置为空
        getWindow().setBackgroundDrawable(null);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView() {
        StateAppBar.translucentStatusBar(this, true);
        CountDownView cdvTime = findViewById(R.id.cdv_time);
        cdvTime.setVisibility(View.GONE);
        if (SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.KEY_FIRST_SPLASH, true)) {
            ActivityUtils.startActivity(SplashPagerActivity.class);
        } else {
            ActivityUtils.startActivity(GuideActivity.class);
        }
        finish();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


    /**
     * 屏蔽物理返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

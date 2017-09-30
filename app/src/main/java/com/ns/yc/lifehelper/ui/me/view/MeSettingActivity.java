package com.ns.yc.lifehelper.ui.me.view;

import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.weight.SwitchButton;
import com.ns.yc.lifehelper.ui.weight.ViewLoading;

import butterknife.Bind;

/**
 * Created by PC on 2017/9/12.
 * 作者：PC
 */

public class MeSettingActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_title_left)
    TextView tvTitleLeft;
    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_right_img)
    ImageView ivRightImg;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.tv_title_right)
    TextView tvTitleRight;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rl_set_go_star)
    RelativeLayout rlSetGoStar;
    @Bind(R.id.switch_button)
    SwitchButton switchButton;
    @Bind(R.id.switch_night)
    SwitchButton switchNight;
    @Bind(R.id.tv_set_cache_size)
    TextView tvSetCacheSize;
    @Bind(R.id.rl_set_clean_cache)
    RelativeLayout rlSetCleanCache;
    @Bind(R.id.rl_set_revise_pwd)
    RelativeLayout rlSetRevisePwd;
    @Bind(R.id.tv_is_cert)
    TextView tvIsCert;
    @Bind(R.id.rl_set_phone)
    RelativeLayout rlSetPhone;
    @Bind(R.id.rl_set_about_us)
    RelativeLayout rlSetAboutUs;
    @Bind(R.id.rl_set_binding)
    RelativeLayout rlSetBinding;
    @Bind(R.id.tv_exit)
    TextView tvExit;
    private ViewLoading mLoading;

    @Override
    public int getContentView() {
        return R.layout.activity_me_setting;
    }

    @Override
    public void initView() {
        initToolBar();
        initLoading();
    }

    private void initToolBar() {
        toolbarTitle.setText("设置中心");
    }

    private void initLoading() {
        // 添加Loading
        mLoading = new ViewLoading(this) {
            @Override
            public void loadCancel() {

            }
        };
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }
        }, 2000);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }
}

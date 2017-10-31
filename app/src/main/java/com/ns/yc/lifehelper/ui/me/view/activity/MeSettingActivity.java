package com.ns.yc.lifehelper.ui.me.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SDCardUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.me.contract.MeSettingContract;
import com.ns.yc.lifehelper.ui.me.presenter.MeSettingPresenter;
import com.ns.yc.lifehelper.utils.FileCacheUtils;
import com.ns.yc.ycutilslib.loadingDialog.ViewLoading;
import com.ns.yc.ycutilslib.switchButton.SwitchButton;
import com.pedaily.yc.ycdialoglib.toast.ToastUtil;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/12
 * 描    述：我的设置中心页面
 * 修订历史：
 * ================================================
 */
public class MeSettingActivity extends BaseActivity implements View.OnClickListener, MeSettingContract.View, SwitchButton.OnCheckedChangeListener {

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
    @Bind(R.id.tv_girl)
    TextView tvGirl;
    @Bind(R.id.switch_pic)
    SwitchButton switchPic;
    @Bind(R.id.tv_pic)
    AppCompatTextView tvPic;
    @Bind(R.id.switch_girl)
    SwitchButton switchGirl;
    @Bind(R.id.switch_random)
    SwitchButton switchRandom;
    @Bind(R.id.tv_pic_state)
    TextView tvPicState;
    @Bind(R.id.ll_pic_quality)
    LinearLayout llPicQuality;
    private ViewLoading mLoading;

    private MeSettingPresenter presenter = new MeSettingPresenter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

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

    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        rlSetGoStar.setOnClickListener(this);
        llPicQuality.setOnClickListener(this);
        rlSetCleanCache.setOnClickListener(this);

        switchNight.setOnCheckedChangeListener(this);
        switchPic.setOnCheckedChangeListener(this);
        switchRandom.setOnCheckedChangeListener(this);
        switchGirl.setOnCheckedChangeListener(this);
        switchButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.rl_set_go_star:
                presenter.goToStar(this);
                break;
            case R.id.ll_pic_quality:
                if(switchPic.isChecked()){
                    presenter.showPicQualityDialog(this);
                }else {
                    ToastUtil.showToast(this,"请开启显示缩略图");
                }
                break;
            case R.id.rl_set_clean_cache:
                presenter.cleanAppCache(this);
                break;
        }
    }


    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.switch_button:            //极光推送

                break;
            case R.id.switch_night:             //夜间模式

                break;
            case R.id.switch_pic:               //是否显示list上图片
                switchPic.setChecked(isChecked);
                presenter.saveIsListShowImg(isChecked);
                break;
            case R.id.switch_random:            //是否随机展示图片
                if(switchGirl.isChecked()){
                    switchRandom.setChecked(isChecked);
                    presenter.saveIsLauncherShowImg(isChecked);
                }else {
                    switchRandom.setChecked(false);
                    ToastUtil.showToast(this,"请开启启动页显示美女图");
                }
                break;
            case R.id.switch_girl:              //启动页是否展示美女图片
                switchGirl.setChecked(isChecked);
                presenter.saveIsLauncherAlwaysShowImg(isChecked);
                break;
        }
    }

    /**
     * 开始loading加载
     */
    @Override
    public void startLoading() {
        // 添加Loading
        mLoading = new ViewLoading(this, Constant.loadingStyle) {
            @Override
            public void loadCancel() {

            }
        };
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }
        }, 2000);
    }


    /**
     * 初始化缩略图开关的状态
     */
    @Override
    public void changeSwitchState(boolean isChecked) {
        switchPic.setChecked(isChecked);
    }

    /**
     * 初始化是否显示妹子图开关的状态
     */
    @Override
    public void changeIsShowLauncherImgSwitchState(boolean isChecked) {
        switchGirl.setChecked(isChecked);
    }

    /**
     * 启动页是否概率出现
     */
    @Override
    public void changeIsAlwaysShowLauncherImgSwitchState(boolean isChecked) {
        switchRandom.setChecked(isChecked);
    }

    /**
     * 设置缩略图不可点击
     */
    @Override
    public void setImageQualityChooseUnEnable() {
        switchPic.setClickable(false);
        tvPicState.setTextColor(getResources().getColor(R.color.gray2));
    }

    /**
     * 设置缩略图可点击
     */
    @Override
    public void setImageQualityChooseEnable() {
        switchPic.setClickable(true);
        tvPicState.setTextColor(getResources().getColor(R.color.blackText4));
    }

    /**
     * 设置随机选项不可点击
     */
    @Override
    public void setLauncherImgProbabilityUnEnable() {
        switchGirl.setClickable(false);
        tvGirl.setTextColor(getResources().getColor(R.color.gray2));
    }

    /**
     * 设置随机选项可点击
     */
    @Override
    public void setLauncherImgProbabilityEnable() {
        switchGirl.setClickable(true);
        tvGirl.setTextColor(getResources().getColor(R.color.blackText4));
    }

    /**
     * 设置缩略图质量
     */
    @Override
    public void setThumbnailQualityInfo(int quality) {
        String thumbnailQuality = "";
        switch (quality) {
            case 0:
                thumbnailQuality = "原图";
                break;
            case 1:
                thumbnailQuality = "默认";
                break;
            case 2:
                thumbnailQuality = "省流";
                break;
        }
        tvPic.setText(thumbnailQuality);
    }

    /**
     * 设置缓存的大小
     */
    @Override
    public void showCacheSize() {
        //目前只是展示内部缓存的大小
        try {
            if (SDCardUtils.isSDCardEnable()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (this.getCacheDir() == null || this.getCodeCacheDir() == null) {
                        return;
                    }
                }
                String cacheSize = FileCacheUtils.getCacheSize(this.getCacheDir());
                //String cacheCodeSize = FileCacheUtils.getCacheSize(this.getCodeCacheDir());
                tvSetCacheSize.setText(cacheSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置清理后的文本
     */
    @Override
    public void setClearText() {
        tvSetCacheSize.setText("0K");
    }


}

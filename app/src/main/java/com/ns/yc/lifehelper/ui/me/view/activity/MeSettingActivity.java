package com.ns.yc.lifehelper.ui.me.view.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.model.bean.UpdateBean;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.ns.yc.lifehelper.ui.me.contract.MeSettingContract;
import com.ns.yc.lifehelper.ui.me.presenter.MeSettingPresenter;
import com.ns.yc.lifehelper.utils.AppToolUtils;
import com.ns.yc.lifehelper.utils.FileCacheUtils;
import com.ns.yc.ycutilslib.loadingDialog.ViewLoading;
import com.ns.yc.ycutilslib.switchButton.SwitchButton;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cn.ycbjie.ycthreadpoollib.PoolThread;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/9/12
 *     desc  : 我的设置中心页面
 *     revise:
 * </pre>
 */

public class MeSettingActivity extends BaseActivity<MeSettingPresenter> implements View.OnClickListener,
        MeSettingContract.View, SwitchButton.OnCheckedChangeListener {

    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.ll_search)
    FrameLayout llSearch;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_set_go_star)
    RelativeLayout rlSetGoStar;
    @BindView(R.id.switch_button)
    SwitchButton switchButton;
    @BindView(R.id.switch_night)
    SwitchButton switchNight;
    @BindView(R.id.tv_set_cache_size)
    TextView tvSetCacheSize;
    @BindView(R.id.rl_set_clean_cache)
    RelativeLayout rlSetCleanCache;
    @BindView(R.id.rl_set_revise_pwd)
    RelativeLayout rlSetRevisePwd;
    @BindView(R.id.tv_is_cert)
    TextView tvIsCert;
    @BindView(R.id.rl_set_phone)
    RelativeLayout rlSetPhone;
    @BindView(R.id.rl_set_about_us)
    RelativeLayout rlSetAboutUs;
    @BindView(R.id.rl_set_binding)
    RelativeLayout rlSetBinding;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.tv_girl)
    TextView tvGirl;
    @BindView(R.id.switch_pic)
    SwitchButton switchPic;
    @BindView(R.id.tv_pic)
    AppCompatTextView tvPic;
    @BindView(R.id.switch_girl)
    SwitchButton switchGirl;
    @BindView(R.id.switch_random)
    SwitchButton switchRandom;
    @BindView(R.id.tv_pic_state)
    TextView tvPicState;
    @BindView(R.id.ll_pic_quality)
    LinearLayout llPicQuality;
    @BindView(R.id.rl_set_feedback)
    RelativeLayout rlSetFeedback;
    @BindView(R.id.rl_set_update)
    RelativeLayout rlSetUpdate;
    @BindView(R.id.tv_update_name)
    TextView tvUpdateName;

    private ViewLoading mLoading;
    private MeSettingPresenter presenter = new MeSettingPresenter(this);
    private String appVersionName;


    @Override
    public int getContentView() {
        return R.layout.activity_me_setting;
    }

    @Override
    public void initView() {
        initToolBar();
        setRipperView();
    }

    private void setRipperView() {
        AppToolUtils.setRipper(rlSetGoStar);
        AppToolUtils.setRipper(llPicQuality);
        AppToolUtils.setRipper(rlSetCleanCache);
        AppToolUtils.setRipper(rlSetFeedback);
        AppToolUtils.setRipper(rlSetUpdate);
        AppToolUtils.setRipper(tvExit);
    }

    private void initToolBar() {
        toolbarTitle.setText("设置中心");
        appVersionName = AppUtils.getAppVersionName();
        tvUpdateName.setText(String.format("当前版本号 v%s", appVersionName));
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        rlSetGoStar.setOnClickListener(this);
        llPicQuality.setOnClickListener(this);
        rlSetCleanCache.setOnClickListener(this);
        rlSetFeedback.setOnClickListener(this);
        rlSetUpdate.setOnClickListener(this);
        tvExit.setOnClickListener(this);

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
                if (switchPic.isChecked()) {
                    presenter.showPicQualityDialog(this);
                } else {
                    ToastUtil.showToast(this, "请开启显示缩略图");
                }
                break;
            case R.id.rl_set_clean_cache:
                presenter.cleanAppCache(this);
                break;
            case R.id.rl_set_feedback:
                startActivity(MeFeedBackActivity.class);
                break;
            case R.id.rl_set_update:
                presenter.checkVersion(appVersionName);
                break;
            case R.id.tv_exit:
                presenter.exitLogout();
                break;
            default:
                break;
        }
    }


    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.switch_button:

                break;
            case R.id.switch_night:

                break;
            case R.id.switch_pic:
                switchPic.setChecked(isChecked);
                presenter.saveIsListShowImg(isChecked);
                break;
            case R.id.switch_random:
                if (switchGirl.isChecked()) {
                    switchRandom.setChecked(isChecked);
                    presenter.saveIsLauncherShowImg(isChecked);
                } else {
                    switchRandom.setChecked(false);
                    ToastUtil.showToast(this, "请开启启动页显示美女图");
                }
                break;
            case R.id.switch_girl:
                switchGirl.setChecked(isChecked);
                presenter.saveIsLauncherAlwaysShowImg(isChecked);
                break;
            default:
                break;
        }
    }

    /**
     * 开始loading加载
     */
    @Override
    public void startLoading() {
        // 添加Loading
        mLoading = new ViewLoading(this, R.style.loading_dialog,"") {
            @Override
            public void loadCancel() {

            }
        };
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
        PoolThread executor = BaseApplication.getInstance().getExecutor();
        executor.setName("load");
        executor.setDelay(2, TimeUnit.MILLISECONDS);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }
        });
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
            default:
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

    @Override
    public void setErrorView(Throwable e) {
        ToastUtil.showToast(this,"加载错误"+e.getMessage());
    }

    @Override
    public void showUpdateDialog(UpdateBean updateBean) {
        StringBuilder content = new StringBuilder("版本号: v");
        content.append(updateBean.getCode());
        content.append("\r\n");
        content.append("版本大小: ");
        content.append(updateBean.getSize());
        content.append("\r\n");
        content.append("更新内容:\r\n");
        content.append(updateBean.getDes().replace("\\r\\n","\r\n"));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("检测到新版本!");
        builder.setMessage(content);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //写更新逻辑
            }
        });
        builder.show();
    }


    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void exitLogout() {
        //跳转首页
        MainActivity.startActivity(this,MainActivity.FIND);
    }


}

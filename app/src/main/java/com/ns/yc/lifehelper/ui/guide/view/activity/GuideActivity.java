package com.ns.yc.lifehelper.ui.guide.view.activity;


import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.guide.contract.GuideContract;
import com.ns.yc.lifehelper.ui.guide.presenter.GuidePresenter;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.ns.yc.yccountdownviewlib.CountDownView;
import com.yc.imageserver.utils.GlideImageUtils;
import com.ycbjie.library.base.mvp.BaseActivity;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 倒计时广告页面
 *     revise:
 * </pre>
 */
public class GuideActivity extends BaseActivity<GuidePresenter> implements
        GuideContract.View ,View.OnClickListener {

    private ImageView ivSplashAd;
    private CountDownView cdvTime;

    private GuideContract.Presenter presenter = new GuidePresenter(this);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cdvTime!=null && cdvTime.isShown()){
            cdvTime.stop();
        }
    }

    /**
     * 屏蔽返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public int getContentView() {
        return R.layout.base_activity_guide;
    }



    @Override
    public void initView() {
        StateAppBar.translucentStatusBar(this, true);
        initFindViewById();
        presenter.startGuideImage();
        initCountDownView();
    }

    private void initFindViewById() {
        ivSplashAd = findViewById(R.id.iv_splash_ad);
        cdvTime = findViewById(R.id.cdv_time);
    }


    @Override
    public void initListener() {
        cdvTime.setOnClickListener(this);
    }

    @Override
    public void initData() {
        presenter.cacheFindNewsData();
        presenter.cacheFindBottomNewsData();
        presenter.cacheHomePileData();
    }


    private void initCountDownView() {
        cdvTime.setTime(5);
        cdvTime.start();
        cdvTime.setOnLoadingFinishListener(this::toMainActivity);
    }

    private void toMainActivity() {
        ActivityUtils.startActivity(MainActivity.class,
                R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        //finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cdv_time:
                cdvTime.stop();
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 展示图片
     */
    @Override
    public void showGuideLogo(String logo) {
        GlideImageUtils.loadImageNet(this,logo,R.drawable.bg_cloud_night,ivSplashAd,false);
    }




}

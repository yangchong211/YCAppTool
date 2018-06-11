package com.ns.yc.lifehelper.ui.guide.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.guide.contract.GuideContract;
import com.ns.yc.lifehelper.ui.guide.presenter.GuidePresenter;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.ns.yc.lifehelper.utils.image.ImageUtils;
import com.ns.yc.yccountdownviewlib.CountDownView;
import com.squareup.picasso.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import cn.ycbjie.ycstatusbarlib.bar.YCAppBar;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 启动页面
 *     revise:
 * </pre>
 */
public class GuideActivity extends BaseActivity<GuidePresenter> implements
        GuideContract.View ,View.OnClickListener {

    @Bind(R.id.iv_splash_ad)
    ImageView ivSplashAd;
    @Bind(R.id.cdv_time)
    CountDownView cdvTime;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YCAppBar.translucentStatusBar(this, true);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView() {
        LogUtils.e("GuideActivity"+"------"+"initView");
        presenter.startGuideImage();
        initCountDownView();
    }


    @Override
    public void initListener() {
        cdvTime.setOnClickListener(this);
    }

    @Override
    public void initData() {
        presenter.cacheHomeNewsData();
        presenter.cacheFindNewsData();
        presenter.cacheFindBottomNewsData();
        presenter.cacheHomePileData();
    }


    private void initCountDownView() {
        cdvTime.setTime(5);
        cdvTime.start();
        cdvTime.setOnLoadingFinishListener(new CountDownView.OnLoadingFinishListener() {
            @Override
            public void finish() {
                toMainActivity();
            }
        });
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
     * 直接跳转主页面
     */
    private void toMainActivity() {
        ActivityUtils.startActivity(MainActivity.class,R.anim.screen_zoom_in,R.anim.screen_zoom_out);
        finish();
    }

    /**
     * 展示图片
     */
    @Override
    public void showGuideLogo(String logo) {
        Callback callback = new Callback() {
            @Override
            public void onSuccess() {
                //加载成功
            }

            @Override
            public void onError() {
                //加载失败
                ivSplashAd.setBackgroundResource(R.drawable.bg_cloud_night);
            }
        };
        LogUtils.e("图片"+logo);
        ImageUtils.loadImgByPicasso(this,logo,R.drawable.bg_cloud_night,ivSplashAd,callback);
    }

}

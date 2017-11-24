package com.ns.yc.lifehelper.ui.guide.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.guide.contract.GuideContract;
import com.ns.yc.lifehelper.ui.guide.presenter.GuidePresenter;
import com.ns.yc.lifehelper.ui.main.view.activity.MainActivity;
import com.ns.yc.lifehelper.utils.ImageUtils;
import com.ns.yc.yccountdownviewlib.CountDownView;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/18
 * 描    述：启动页面
 * 修订历史：
 * ================================================
 */
public class GuideActivity extends BaseActivity implements GuideContract.View ,View.OnClickListener {

    @Bind(R.id.iv_splash_ad)
    ImageView ivSplashAd;
    @Bind(R.id.tv_splash_skip_ad)
    TextView tvSplashSkipAd;
    @Bind(R.id.iv_splash_download)
    ImageView ivSplashDownload;
    @Bind(R.id.cdv_time)
    CountDownView cdvTime;

    private GuideContract.Presenter presenter = new GuidePresenter(this);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cdvTime!=null && cdvTime.isShown()){
            cdvTime.stop();
        }
        presenter.unSubscribe();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView() {
        initState();
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
    }

    private void initCountDownView() {
        cdvTime.setTime(5);
        cdvTime.start();
        cdvTime.setOnLoadingFinishListener(new CountDownView.OnLoadingFinishListener() {
            @Override
            public void finish() {
                presenter.goMainActivity();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_splash_skip_ad:
                presenter.goMainActivity();
                break;
            case R.id.cdv_time:
                cdvTime.stop();
                finish();
                break;
        }
    }

    /**
     * 直接跳转主页面
     */
    @Override
    public void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();
    }

    /**
     * 展示图片
     */
    @Override
    public void showGuideLogo(String logo) {
        ImageUtils.loadImgByPicassoError(this,logo,R.drawable.pic_page_background,ivSplashAd);
        /*Picasso.with(this)
                .load(logo)
                .placeholder(R.drawable.pic_page_background)
                .into(ivSplashAd, new Callback() {
                    @Override
                    public void onSuccess() {
                        //加载成功
                    }

                    @Override
                    public void onError() {
                        //加载失败
                    }
                });*/
    }

    @Override
    public Activity getActivity() {
        return this;
    }


}

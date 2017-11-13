package com.ns.yc.lifehelper.ui.guide.presenter;

import android.text.TextUtils;

import com.ns.yc.lifehelper.api.ConstantImageApi;
import com.ns.yc.lifehelper.base.BaseConfig;
import com.ns.yc.lifehelper.ui.guide.contract.GuideContract;

import java.util.Random;

import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：启动页
 * 修订历史：
 * ================================================
 */
public class GuidePresenter implements GuideContract.Presenter {

    private GuideContract.View mView;
    private CompositeSubscription mSubscriptions;

    public GuidePresenter(GuideContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        if(BaseConfig.INSTANCE.isShowGirlImg()){
            String bannerUrl = BaseConfig.INSTANCE.getBannerUrl();
            if(TextUtils.isEmpty(bannerUrl)){
                int i = new Random().nextInt(ConstantImageApi.SPALSH_URLS.length);
                String splashUrl = ConstantImageApi.SPALSH_URLS[i];
                mView.showGuideLogo(splashUrl);
            }else {
                mView.showGuideLogo(bannerUrl);
            }
        } else {
            // 先显示默认图
            int i = new Random().nextInt(ConstantImageApi.SPALSH_URLS.length);
            String splashUrl = ConstantImageApi.SPALSH_URLS[i];
            mView.showGuideLogo(splashUrl);
        }
    }


    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void goMainActivity() {
        mView.toMainActivity();
    }
}

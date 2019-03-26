package com.ycbjie.zhihu.web.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.db.cache.CacheZhLike;
import com.ycbjie.library.db.realm.RealmDbHelper;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.library.utils.rxUtils.RxUtil;
import com.ycbjie.zhihu.api.ZhiHuModel;
import com.ycbjie.zhihu.model.ZhiHuDetailExtraBean;
import com.ycbjie.zhihu.model.ZhihuDetailBean;
import com.ycbjie.zhihu.web.contract.WebViewAnimContract;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/30V
 * 描    述：可动画的详情页面，用腾讯x5库的webView
 * 修订历史：
 * ================================================
 */
public class WebViewAnimPresenter implements WebViewAnimContract.Presenter {

    private WebViewAnimContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Activity activity;
    private ZhihuDetailBean mData;


    public WebViewAnimPresenter(WebViewAnimContract.View androidView) {
        this.mView = androidView;
        this.activity = (Activity) androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
        activity = null;
    }


    @Override
    public void bindView(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void getDetailData(int id) {
        ZhiHuModel model = ZhiHuModel.getInstance();
        Subscription rxSubscription =  model.getDetailInfo(id)
                .compose(RxUtil.<ZhihuDetailBean>rxSchedulerHelper())
                .subscribe(new Subscriber<ZhihuDetailBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onNext(ZhihuDetailBean zhihuDetailBean) {
                        mData = zhihuDetailBean;
                        mView.showContent(zhihuDetailBean);
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getExtraData(int id) {
        ZhiHuModel model = ZhiHuModel.getInstance();
        Subscription rxSubscription =  model.getDetailExtraInfo(id)
                .compose(RxUtil.<ZhiHuDetailExtraBean>rxSchedulerHelper())
                .subscribe(new Subscriber<ZhiHuDetailExtraBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onNext(ZhiHuDetailExtraBean zhiHuDetailExtraBean) {
                        mView.showExtraInfo(zhiHuDetailExtraBean);
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void deleteLikeData() {
        if (mData != null) {
            RealmDbHelper.getInstance().deleteLikeBean(String.valueOf(mData.getId()));
            ToastUtils.showShort("删除收藏成功");
        } else {
            ToastUtils.showShort("操作失败");
        }
    }

    @Override
    public void insertLikeData() {
        if (mData != null) {
            CacheZhLike bean = new CacheZhLike();
            bean.setId(String.valueOf(mData.getId()));
            bean.setImage(mData.getImage());
            bean.setTitle(mData.getTitle());
            bean.setType(Constant.LikeType.TYPE_ZHI_HU);
            bean.setTime(System.currentTimeMillis());
            RealmDbHelper.getInstance().insertLikeBean(bean);
            ToastUtils.showShort("添加收藏成功");
        } else {
            ToastUtils.showShort("操作失败");
        }
    }

}

package com.ns.yc.lifehelper.ui.webView.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.comment.Constant;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.db.cache.CacheZhLike;
import com.ns.yc.lifehelper.ui.webView.contract.WebViewAnimContract;
import com.ns.yc.lifehelper.api.http.zhihu.ZhiHuModel;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhihuDetailBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDetailExtraBean;
import com.ns.yc.lifehelper.db.realm.RealmDbHelper;
import com.ns.yc.lifehelper.utils.rxUtils.RxUtil;

import io.realm.Realm;
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
    private Realm realm;


    public WebViewAnimPresenter(WebViewAnimContract.View androidView) {
        this.mView = androidView;
        this.activity = (Activity) androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        initRealm();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
        activity = null;
    }

    private void initRealm() {
        realm = BaseApplication.getInstance().getRealmHelper();
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

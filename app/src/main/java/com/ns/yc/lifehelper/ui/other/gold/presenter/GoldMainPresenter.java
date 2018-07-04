package com.ns.yc.lifehelper.ui.other.gold.presenter;

import android.support.annotation.NonNull;

import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.ui.other.gold.contract.GoldMainContract;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldManagerBean;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldManagerItemBean;
import com.ns.yc.lifehelper.db.realm.RealmDbHelper;
import com.ns.yc.lifehelper.utils.rxUtils.RxBus;

import io.realm.Realm;
import io.realm.RealmList;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GoldMainPresenter implements GoldMainContract.Presenter {

    private GoldMainContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private RealmList<GoldManagerItemBean> mList;
    private boolean isFirst = true;
    private Realm realm;

    public GoldMainPresenter(GoldMainContract.View homeView) {
        this.mView = homeView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        isFirst = true;
        registerEvent();
    }

    private void registerEvent() {
        /*new SerializedSubject<>(PublishSubject.create())
                .ofType(GoldManagerBean.class)
                .compose(RxUtil.<GoldManagerBean>rxSchedulerHelper())
                .subscribe(new Action1<GoldManagerBean>() {
                    @Override
                    public void call(GoldManagerBean goldManagerBean) {
                        RealmHelper.updateGoldManagerList(realm,goldManagerBean);
                        mView.updateTab(goldManagerBean.getManagerList());
                    }
                });*/
        Subscription subscription = RxBus.getDefault().toDefaultObservable(
                GoldManagerBean.class, new Action1<GoldManagerBean>() {
            @Override
            public void call(GoldManagerBean goldManagerBean) {
                RealmDbHelper.getInstance().updateGoldManagerList(goldManagerBean);
                mView.updateTab(goldManagerBean.getManagerList());
            }
        });
        mSubscriptions.add(subscription);
    }


    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void initRealm() {
        realm = BaseApplication.getInstance().getRealmHelper();
    }

    @Override
    public void setManagerList() {
        mView.jumpToManager(RealmDbHelper.getInstance().getGoldManagerList());
    }


    @Override
    public void initManagerList() {
        if (isFirst) {
            //第一次使用，生成默认ManagerList
            initList();
            RealmDbHelper.getInstance().updateGoldManagerList(new GoldManagerBean(mList));
            mView.updateTab(mList);
        } else {
            if (RealmDbHelper.getInstance().getGoldManagerList() == null) {
                initList();
                RealmDbHelper.getInstance().updateGoldManagerList(new GoldManagerBean(mList));
            } else {
                //noinspection ConstantConditions
                mList = RealmDbHelper.getInstance().getGoldManagerList().getManagerList();
            }
            mView.updateTab(mList);
        }
    }


    private void initList() {
        mList = new RealmList<>();
        mList.add(new GoldManagerItemBean(0, true));
        mList.add(new GoldManagerItemBean(1, true));
        mList.add(new GoldManagerItemBean(2, true));
        mList.add(new GoldManagerItemBean(3, true));
        mList.add(new GoldManagerItemBean(4, true));
        mList.add(new GoldManagerItemBean(5, false));
        mList.add(new GoldManagerItemBean(6, false));
        mList.add(new GoldManagerItemBean(7, false));
    }


}

package com.ns.yc.lifehelper.ui.other.gold.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ns.yc.lifehelper.ui.other.gold.contract.GoldPagerContract;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldListBean;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldManagerItemBean;
import com.ns.yc.lifehelper.ui.other.gold.model.api.GoldModel;
import com.ns.yc.lifehelper.utils.RxUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import rx.Subscriber;
import rx.Subscription;
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
public class GoldPagerPresenter implements GoldPagerContract.Presenter {

    private GoldPagerContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private RealmList<GoldManagerItemBean> mList;
    private boolean isFirst = true;
    private Realm realm;
    private String mType;
    private List<GoldListBean> totalList = new ArrayList<>();
    private boolean isHotList = true;
    private int currentPage = 0;
    private static final int NUM_EACH_PAGE = 20;
    private static final int NUM_HOT_LIMIT = 3;

    public GoldPagerPresenter(GoldPagerContract.View homeView) {
        this.mView = homeView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        isFirst = true;

    }


    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void getGoldData(String mType) {
        this.mType = mType;
        currentPage = 0;
        totalList.clear();

        GoldModel model = GoldModel.getInstance();
        Subscription subscribe = model.fetchGoldList(mType, NUM_EACH_PAGE, currentPage++)
                .compose(RxUtil.<List<GoldListBean>>rxSchedulerHelper())
                .subscribe(new Subscriber<List<GoldListBean>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("GoldPagerPresenter","onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("GoldPagerPresenter","onError"+e.getMessage());
                    }

                    @Override
                    public void onNext(List<GoldListBean> goldListBeen) {
                        Log.e("GoldPagerPresenter","onNext");
                        if (isHotList) {
                            isHotList = false;
                            totalList.addAll(goldListBeen);
                        } else {
                            isHotList = true;
                            totalList.addAll(goldListBeen);
                            mView.showContent(totalList);
                        }
                    }
                });
        mSubscriptions.add(subscribe);


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);

        Subscription hotSubscribe = model.fetchGoldHotList(mType,
                new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()), NUM_HOT_LIMIT)
                .compose(RxUtil.<List<GoldListBean>>rxSchedulerHelper())
                .subscribe(new Subscriber<List<GoldListBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<GoldListBean> goldListBeen) {
                        if(goldListBeen!=null){
                            Log.e("Tag","----");
                        }
                    }
                });
        mSubscriptions.add(hotSubscribe);
    }
}

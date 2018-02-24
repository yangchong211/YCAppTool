package com.ns.yc.lifehelper.ui.other.zhihu.presenter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.ui.other.zhihu.contract.ZhiHuDailyContract;
import com.ns.yc.lifehelper.api.http.zhihu.ZhiHuModel;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDailyBeforeListBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDailyListBean;
import com.ns.yc.lifehelper.ui.other.zhihu.model.db.RealmHelper;
import com.ns.yc.lifehelper.utils.rx.RxBus;
import com.ns.yc.lifehelper.utils.rx.RxUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/29
 * 描    述：知乎日报模块        日报
 * 修订历史：
 * ================================================
 */
public class ZhiHuDailyPresenter implements ZhiHuDailyContract.Presenter {

    private ZhiHuDailyContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private Realm realm;
    private Subscription intervalSubscription;
    private int topCount = 0;
    private int currentTopCount = 0;


    public ZhiHuDailyPresenter(ZhiHuDailyContract.View homeView) {
        this.mView = homeView;
        mSubscriptions = new CompositeSubscription();
        registerEvent();
    }

    @Override
    public void subscribe() {
        initRealm();
    }


    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    private void initRealm() {
        if(realm ==null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }

    private void registerEvent() {
        initRealm();
        Subscription rxSubscription = RxBus.getDefault().toObservable(CalendarDay.class)
                .subscribeOn(Schedulers.io())
                .map(new Func1<CalendarDay, String>() {
                    @Override
                    public String call(CalendarDay calendarDay) {
                        StringBuilder date = new StringBuilder();
                        String year = String.valueOf(calendarDay.getYear());
                        String month = String.valueOf(calendarDay.getMonth() + 1);
                        String day = String.valueOf(calendarDay.getDay() + 1);
                        if(month.length() < 2) {
                            month = "0" + month;
                        }
                        if(day.length() < 2) {
                            day = "0" + day;
                        }
                        return date.append(year).append(month).append(day).toString();
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                        if(s.equals(TimeUtils.getNowString(format))) {
                            getData();
                            return false;
                        }
                        return true;
                    }
                })
                .observeOn(Schedulers.io())   //为了网络请求切到io线程
                .flatMap(new Func1<String, Observable<ZhiHuDailyBeforeListBean>>() {
                    @Override
                    public Observable<ZhiHuDailyBeforeListBean> call(String date) {
                        ZhiHuModel model = ZhiHuModel.getInstance();
                        return model.fetchDailyBeforeListInfo(date);
                }
                })
                .observeOn(AndroidSchedulers.mainThread())    //为了使用Realm和显示结果切到主线程
                .map(new Func1<ZhiHuDailyBeforeListBean, ZhiHuDailyBeforeListBean>() {
                    @Override
                    public ZhiHuDailyBeforeListBean call(ZhiHuDailyBeforeListBean dailyBeforeListBean) {
                        List<ZhiHuDailyListBean.StoriesBean> list = dailyBeforeListBean.getStories();
                        for(ZhiHuDailyListBean.StoriesBean item : list) {
                            item.setReadState(RealmHelper.queryNewsId(realm,item.getId()));
                        }
                        return dailyBeforeListBean;
                    }
                })
                .subscribe(new Subscriber<ZhiHuDailyBeforeListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        /*if(NetworkUtils.isConnected()){
                            mView.setErrorView();
                        }else {
                            mView.setNetworkErrorView();
                        }*/
                    }

                    @Override
                    public void onNext(ZhiHuDailyBeforeListBean zhiHuDailyBeforeListBean) {
                        if(zhiHuDailyBeforeListBean!=null){
                            int year = Integer.valueOf(zhiHuDailyBeforeListBean.getDate().substring(0,4));
                            int month = Integer.valueOf(zhiHuDailyBeforeListBean.getDate().substring(4,6));
                            int day = Integer.valueOf(zhiHuDailyBeforeListBean.getDate().substring(6,8));
                            @SuppressLint("DefaultLocale")
                            String time = String.format("%d年%d月%d日", year, month, day);
                            mView.showMoreContent(time,zhiHuDailyBeforeListBean);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getData() {
        ZhiHuModel model = ZhiHuModel.getInstance();
        Subscription rxSubscription = model.getDailyList()
                .compose(RxUtil.<ZhiHuDailyListBean>rxSchedulerHelper())
                .subscribe(new Subscriber<ZhiHuDailyListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(NetworkUtils.isConnected()){
                            mView.setErrorView();
                        }else {
                            mView.setNetworkErrorView();
                        }
                    }

                    @Override
                    public void onNext(ZhiHuDailyListBean zhiHuDailyBean) {
                        if(zhiHuDailyBean!=null){
                            mView.setView(zhiHuDailyBean);
                        }else {
                            mView.setEmptyView();
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void insertReadToDB(int id) {
        initRealm();
        RealmHelper.insertNewsId(realm , id);
    }

    @Override
    public void stopInterval() {
        if (intervalSubscription != null) {
            intervalSubscription.unsubscribe();
        }
    }

    @Override
    public void startInterval() {
        intervalSubscription = Observable.interval(6, TimeUnit.SECONDS)
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if(currentTopCount == topCount){
                            currentTopCount = 0;
                        }
                        mView.doInterval(currentTopCount++);
                    }
                });
        mSubscriptions.add(intervalSubscription);
    }

}

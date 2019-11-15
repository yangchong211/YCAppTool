package com.ns.yc.lifehelper.ui.guide.presenter;

import android.app.Activity;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.bean.SelectPoint;
import com.ns.yc.lifehelper.ui.guide.contract.SelectFollowContract;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 关注点页面
 *     revise:
 * </pre>
 */
public class SelectFollowPresenter implements SelectFollowContract.Presenter {

    private SelectFollowContract.View mView;
    private CompositeSubscription mSubscriptions;
    private List<SelectPoint> list = new ArrayList<>();

    public SelectFollowPresenter(SelectFollowContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }


    @Override
    public void unSubscribe() {
        mSubscriptions.unsubscribe();
    }


    @Override
    public void addData(Activity activity) {
        String[] titles = activity.getResources().getStringArray(R.array.select_follow);
        for(int a=0 ; a<titles.length ; a++){
            SelectPoint selectPoint = new SelectPoint();
            selectPoint.setName(titles[a]);
            list.add(selectPoint);
        }
        mView.refreshData(list);
    }

}

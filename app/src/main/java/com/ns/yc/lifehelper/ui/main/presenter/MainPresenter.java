package com.ns.yc.lifehelper.ui.main.presenter;

import android.app.Activity;
import android.content.res.TypedArray;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.model.entry.TabEntity;
import com.ns.yc.lifehelper.ui.main.contract.MainContract;

import java.util.ArrayList;

import rx.subscriptions.CompositeSubscription;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/3/18
 * 描    述：Main主页面
 * 修订历史：
 * ================================================
 */
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Activity activity;

    public MainPresenter(MainContract.View androidView) {
        this.mView = androidView;
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
    public ArrayList<CustomTabEntity> getTabEntity() {
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        TypedArray mIconUnSelectIds = activity.getResources().obtainTypedArray(R.array.main_tab_un_select);
        TypedArray mIconSelectIds = activity.getResources().obtainTypedArray(R.array.main_tab_select);
        String[] mainTitles = activity.getResources().getStringArray(R.array.main_title);
        for (int i = 0; i < mainTitles.length; i++) {
            int unSelectId = mIconUnSelectIds.getResourceId(i, R.drawable.tab_home_unselect);
            int selectId = mIconSelectIds.getResourceId(i, R.drawable.tab_home_select);
            mTabEntities.add(new TabEntity(mainTitles[i],selectId , unSelectId));
        }
        mIconUnSelectIds.recycle();
        mIconSelectIds.recycle();
        return mTabEntities;
    }

    /**
     * 版本更新
     * 后期自己制作json文件，暂时先放着
     */
    @Override
    public void getUpdate() {

    }
}

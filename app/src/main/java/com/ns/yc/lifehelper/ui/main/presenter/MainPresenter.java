package com.ns.yc.lifehelper.ui.main.presenter;

import android.content.res.TypedArray;

import com.blankj.utilcode.util.Utils;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.main.contract.MainContract;
import com.ycbjie.library.model.entry.TabEntity;

import java.util.ArrayList;

import rx.subscriptions.CompositeSubscription;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : Main主页面
 *     revise:
 * </pre>
 */
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private CompositeSubscription mSubscriptions;

    public MainPresenter(MainContract.View androidView) {
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
    public ArrayList<CustomTabEntity> getTabEntity() {
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        TypedArray mIconUnSelectIds = Utils.getApp().getResources().obtainTypedArray(R.array.main_tab_un_select);
        TypedArray mIconSelectIds = Utils.getApp().getResources().obtainTypedArray(R.array.main_tab_select);
        String[] mainTitles = Utils.getApp().getResources().getStringArray(R.array.main_title);
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

package com.ns.yc.lifehelper.ui.other.imTalk.presenter;

import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.entry.TabEntity;
import com.ns.yc.lifehelper.ui.other.imTalk.contract.ImTalkContract;

import java.util.ArrayList;

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
public class ImTalkPresenter implements ImTalkContract.Presenter {

    private ImTalkContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private AppCompatActivity activity;

    public ImTalkPresenter(ImTalkContract.View homeView) {
        this.mView = homeView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }


    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void bindView(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public ArrayList<CustomTabEntity> getTabEntity() {
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        TypedArray mIconUnSelectIds = activity.getResources().obtainTypedArray(R.array.im_tab_un_select);
        TypedArray mIconSelectIds = activity.getResources().obtainTypedArray(R.array.im_tab_select);
        String[] mainTitles = activity.getResources().getStringArray(R.array.im_title);
        for (int i = 0; i < mainTitles.length; i++) {
            int unSelectId = mIconUnSelectIds.getResourceId(i, R.drawable.em_conversation_normal);
            int selectId = mIconSelectIds.getResourceId(i, R.drawable.em_conversation_selected);
            mTabEntities.add(new TabEntity(mainTitles[i],selectId , unSelectId));
        }
        mIconUnSelectIds.recycle();
        mIconSelectIds.recycle();
        return mTabEntities;
    }


}

package com.ns.yc.lifehelper.ui.data.presenter;

import android.app.Activity;

import com.ns.yc.lifehelper.ui.data.contract.OpenFileContract;

import java.util.ArrayList;
import java.util.List;

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
public class OpenFilePresenter implements OpenFileContract.Presenter {

    private OpenFileContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Activity activity;

    public OpenFilePresenter(OpenFileContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        activity = mView.getActivity();
    }


    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
        activity = null;
    }

    @Override
    public List<String> getFileData() {
        List<String> data = new ArrayList<>();
        data.add("http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc");
        data.add("http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc");
        data.add("http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc");
        data.add("http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc");
        data.add("http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc");
        data.add("http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc");
        data.add("http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc");
        data.add("http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc");
        return data;
    }

}

package com.ns.yc.lifehelper.base.mvp2;

import android.text.TextUtils;

import com.ns.yc.lifehelper.utils.LogUtils;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;


public abstract class BaseComSubscriber<T> extends Subscriber<T> {

    private BaseMvpView mView;
    private String mErrorMsg;

    protected BaseComSubscriber(BaseMvpView view){
        this.mView = view;
    }

    protected BaseComSubscriber(BaseMvpView view, String errorMsg){
        this.mView = view;
        this.mErrorMsg = errorMsg;
    }

    @Override
    public void onCompleted() {
        LogUtils.e("BaseComSubscriber"+"加载数据完成");
    }

    @Override
    public void onError(Throwable e) {
        if (mView == null)
            return;
        if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)) {
            mView.showError(mErrorMsg);
        } else if (e instanceof ApiException) {
            mView.showError(e.toString());
        } else if (e instanceof HttpException) {
            mView.showError("数据加载失败ヽ(≧Д≦)ノ");
        } else {
            mView.showError("未知错误ヽ(≧Д≦)ノ");
        }
    }


}


class ApiException extends Exception{
    public ApiException(String msg) {
        super(msg);
    }
}

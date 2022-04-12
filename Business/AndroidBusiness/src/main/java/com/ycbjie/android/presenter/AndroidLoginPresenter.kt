package com.ycbjie.android.presenter

import android.annotation.SuppressLint
import com.ycbjie.android.contract.AndroidLoginContract
import com.ycbjie.android.model.helper.AndroidHelper
import com.ycbjie.android.network.BaseSchedulerProvider
import com.ycbjie.android.network.ResponseTransformer
import com.ycbjie.android.network.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class AndroidLoginPresenter : AndroidLoginContract.Presenter {

    private var mView : AndroidLoginContract.View
    private var scheduler: BaseSchedulerProvider? = null

    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    constructor(view : AndroidLoginContract.View){
        this.mView = view
        scheduler = SchedulerProvider.getInstance()
    }

    override fun subscribe() {

    }

    override fun unSubscribe() {
        compositeDisposable.dispose()
    }

    @SuppressLint("CheckResult")
    override fun startLogin(name: String, pwd: String) {
        val instance = AndroidHelper.instance()
        val subscribe = instance.login(name = name, pwd = pwd)
                .compose(ResponseTransformer.handleResult())
                .compose(scheduler?.applySchedulers())
                .subscribe({ bean ->
                    mView.loginSuccess(bean)
                }, { t ->
                    mView.loginError(t.message)
                })
        compositeDisposable.add(subscribe)
    }
}

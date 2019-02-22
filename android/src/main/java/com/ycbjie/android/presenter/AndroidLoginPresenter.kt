package com.ycbjie.android.presenter

import android.annotation.SuppressLint
import com.ycbjie.android.contract.AndroidLoginContract
import com.ycbjie.android.model.helper.AndroidHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AndroidLoginPresenter : AndroidLoginContract.Presenter {

    private var mView : AndroidLoginContract.View

    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    constructor(view : AndroidLoginContract.View){
        this.mView = view
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bean ->
                    mView.loginSuccess(bean)
                }, { t ->
                    mView.loginError(t.message)
                })
        compositeDisposable.add(subscribe)
    }
}

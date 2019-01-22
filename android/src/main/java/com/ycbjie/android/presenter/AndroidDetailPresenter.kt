package com.ycbjie.android.presenter

import com.ycbjie.android.contract.AndroidCollectContract
import io.reactivex.disposables.CompositeDisposable
import com.ycbjie.android.network.BaseSchedulerProvider
import com.ycbjie.android.network.SchedulerProvider

class AndroidDetailPresenter : AndroidCollectContract.Presenter {

    private var mView: AndroidCollectContract.View
    private var scheduler: BaseSchedulerProvider? = null


    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    constructor(androidView: AndroidCollectContract.View){
        this.mView = androidView
        scheduler = SchedulerProvider.getInstatnce()
    }

    override fun subscribe() {

    }

    override fun unSubscribe() {
        compositeDisposable.dispose()
    }

    override fun collectInArticle(articleId: Int) {

    }

    override fun unCollectArticle(articleId: Int) {

    }

}

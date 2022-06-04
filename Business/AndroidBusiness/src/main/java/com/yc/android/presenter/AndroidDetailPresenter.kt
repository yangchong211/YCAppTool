package com.yc.android.presenter

import com.yc.android.contract.AndroidDetailContract
import com.yc.android.network.BaseSchedulerProvider
import com.yc.android.network.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class AndroidDetailPresenter : AndroidDetailContract.Presenter {

    private var mView: AndroidDetailContract.View
    private var scheduler: BaseSchedulerProvider? = null


    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    constructor(androidView: AndroidDetailContract.View){
        this.mView = androidView
        scheduler = SchedulerProvider.getInstance()
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

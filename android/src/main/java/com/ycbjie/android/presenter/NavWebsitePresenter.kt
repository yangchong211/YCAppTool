package com.ycbjie.android.presenter

import com.ycbjie.android.contract.NavWebsiteContract
import com.ycbjie.android.model.helper.AndroidHelper
import com.ycbjie.android.network.ResponseTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import com.ycbjie.android.network.BaseSchedulerProvider
import com.ycbjie.android.network.SchedulerProvider

class NavWebsitePresenter : NavWebsiteContract.Presenter {


    private var mView: NavWebsiteContract.View
    private var scheduler: BaseSchedulerProvider? = null

    constructor(androidView: NavWebsiteContract.View, scheduler: SchedulerProvider){
        this.mView = androidView
        this.scheduler = scheduler
    }

    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun subscribe() {

    }

    override fun unSubscribe() {

    }

    override fun getWebsiteNavi() {
        val instance = AndroidHelper.instance()
        var disapose: Disposable = instance.getNaviJson().compose(scheduler?.applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribe(
                        { t ->
                            mView.getNaviWebSiteSuccess(t)
                        }
                        ,
                        { t: Throwable ->
                            mView.getNaiWebSiteFail(t.message!!)
                        })
        compositeDisposable.add(disapose)
    }


}

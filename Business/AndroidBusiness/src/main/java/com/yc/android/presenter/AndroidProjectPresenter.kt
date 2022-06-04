package com.yc.android.presenter


import com.yc.toolutils.AppLogUtils
import com.yc.toolutils.AppNetworkUtils
import com.yc.android.contract.AndroidProjectContract
import com.yc.android.model.helper.AndroidHelper
import com.yc.android.network.BaseSchedulerProvider
import com.yc.android.network.ResponseTransformer
import com.yc.android.network.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class AndroidProjectPresenter : AndroidProjectContract.Presenter {

    private var mView: AndroidProjectContract.View
    private var page: Int = 0
    private var scheduler: BaseSchedulerProvider? = null

    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }


    constructor(androidView: AndroidProjectContract.View){
        this.mView = androidView
        scheduler = SchedulerProvider.getInstance()
    }


    override fun subscribe() {

    }

    override fun unSubscribe() {
        compositeDisposable.dispose()
    }

    override fun getProjectTree() {
        val instance = AndroidHelper.instance()
        val disposable: Disposable = instance.getProjectTree()
                //网络请求在子线程，所以是在io线程，避免阻塞线程
                /*.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())*/
                .compose(ResponseTransformer.handleResult())
                .compose(scheduler?.applySchedulers())
                .subscribe ({ bean ->
                    mView.setProjectTreeSuccess(bean)
                }, { t ->

                })
        compositeDisposable.add(disposable)
    }

    override fun getProjectTreeList(id: Int, b: Boolean) {
        if (b) {
            page = 0
        }
        val instance = AndroidHelper.instance()
        val disposable: Disposable = instance.getProjectListByCid(page,id)
                //网络请求在子线程，所以是在io线程，避免阻塞线程
                //.compose(ResponseTransformer.handleResult())
                //.compose(scheduler?.applySchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ bean ->
                    mView.setProjectListByCidSuccess(bean, b)
                    page++
                    AppLogUtils.e("getProjectTreeList-----"+"成功"+ (bean.data?.size))
                }, { t ->
                    AppLogUtils.e("getProjectTreeList-----"+"onError"+t.localizedMessage)
                    if(AppNetworkUtils.isConnected()){
                        mView.setProjectListByCidError()
                    }else{
                        mView.setProjectListByCidNetError()
                    }
                })
        compositeDisposable.add(disposable)
    }

}

package com.ycbjie.android.presenter


import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.ycbjie.android.contract.AndroidHomeContract
import com.ycbjie.android.model.model.HomeModel
import com.ycbjie.android.network.BaseSchedulerProvider
import com.ycbjie.android.network.ResponseTransformer
import com.ycbjie.android.network.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class AndroidHomePresenter : AndroidHomeContract.Presenter {

    private var mView: AndroidHomeContract.View
    private var scheduler: BaseSchedulerProvider? = null

    //lazy 委托属性可以用于只读属性的惰性加载
    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    //懒初始化是指推迟一个变量的初始化时机，变量在使用的时候才去实例化，这样会更加的高效。
    //因为我们通常会遇到这样的情况，一个变量直到使用时才需要被初始化，
    //或者仅仅是它的初始化依赖于某些无法立即获得的上下文。
    private val model: HomeModel by lazy {
        HomeModel()
    }

    constructor(androidView: AndroidHomeContract.View){
        this.mView = androidView
        scheduler = SchedulerProvider.getInstance()
    }


    override fun subscribe() {

    }

    override fun unSubscribe() {
        compositeDisposable.dispose()
    }


    override fun getHomeList(page: Int) {
        val disposable: Disposable = model.getHomeList(page)
                //网络请求在子线程，所以是在io线程，避免阻塞线程
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ bean ->
                    LogUtils.e("getHomeList-----"+"onNext")
                    mView.setDataView(bean)
                }, { t ->
                    LogUtils.e("getHomeList-----"+"onError"+t.localizedMessage)
                    if(NetworkUtils.isConnected()){
                        mView.setDataErrorView()
                    }else{
                        mView.setNetWorkErrorView()
                    }
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun getBannerData(isRefresh: Boolean) {
        val disposable: Disposable = model.getBannerData()
                //网络请求在子线程，所以是在io线程，避免阻塞线程
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ bean ->
                        LogUtils.e("getBanner-----"+"onNext")
                        mView.setBannerView(bean)
                    }, { t ->
                        LogUtils.e("getBanner-----"+"onError"+t.localizedMessage)
                    }
                )
        compositeDisposable.add(disposable)
    }

    override fun unCollectArticle(selectId: Int) {
        val disposable = model.unCollectArticle(selectId)
                .compose(ResponseTransformer.handleResult())
                .compose(scheduler?.applySchedulers())
                .subscribe(
                        {
                            mView.unCollectArticleSuccess()
                        }
                        ,
                        {t: Throwable ->
                            mView.unCollectArticleFail(t)
                        }
                )
        compositeDisposable.add(disposable)
    }

    override fun collectInArticle(selectId: Int) {
        val disposable = model.collectInArticle(selectId)
                .compose(ResponseTransformer.handleResult())
                .compose(scheduler?.applySchedulers())
                .subscribe(
                        {
                            mView.collectInArticleSuccess()
                        },
                        { t: Throwable ->
                            mView.collectInArticleFail(t)
                        }
                )
        compositeDisposable.add(disposable)
    }


}

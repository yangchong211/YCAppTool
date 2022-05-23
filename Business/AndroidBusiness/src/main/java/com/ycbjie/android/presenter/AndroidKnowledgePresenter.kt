package com.ycbjie.android.presenter

import com.yc.toolutils.AppLogUtils
import com.yc.toolutils.AppNetworkUtils
import com.ycbjie.android.contract.AndroidKnowledgeContract
import com.ycbjie.android.model.helper.AndroidHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AndroidKnowledgePresenter : AndroidKnowledgeContract.Presenter {

    var mView: AndroidKnowledgeContract.View

    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }


    constructor(androidView: AndroidKnowledgeContract.View){
        this.mView = androidView
    }



    override fun subscribe() {

    }

    override fun unSubscribe() {
        compositeDisposable.dispose()
    }

    override fun getKnowledgeTree() {
        val instance = AndroidHelper.instance()
        val disposable: Disposable = instance.getKnowledgeTree()
                //网络请求在子线程，所以是在io线程，避免阻塞线程
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ bean ->
                    AppLogUtils.e("getHomeList-----"+"成功")
                    mView.getTreeSuccess(bean)
                }, { t ->
                    AppLogUtils.e("getHomeList-----"+"onError"+t.localizedMessage)
                    if(AppNetworkUtils.isConnected()){

                    }else{

                    }
                }
                )
        compositeDisposable.add(disposable)
    }

}

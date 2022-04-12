package com.ycbjie.android.network

import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 线程切换实现类
 */
class SchedulerProvider : BaseSchedulerProvider {

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    //companion object的好处是，外部类可以直接访问对象，不需要通过对象指针
    companion object {
        private var mInstance: SchedulerProvider? = null
        fun getInstance(): SchedulerProvider? {
            if (mInstance == null) {
                synchronized(SchedulerProvider::class) {
                    if (mInstance == null) {
                        mInstance = SchedulerProvider()
                    }
                }
            }
            return mInstance
        }
    }


    /**
     * 切换线程操作
     */
    override fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.observeOn(ui())
                .subscribeOn(io())
        }
    }

}
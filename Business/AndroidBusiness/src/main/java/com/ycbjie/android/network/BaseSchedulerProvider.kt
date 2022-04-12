package com.ycbjie.android.network

import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler

/**
 * 线程切换拓展类
 */
interface BaseSchedulerProvider {
    fun computation(): Scheduler
    fun io() : Scheduler
    fun ui(): Scheduler
    fun <T> applySchedulers(): ObservableTransformer<T, T>
}
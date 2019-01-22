package com.ycbjie.android.network

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function

/**
 * rxjava2变换封装，分离数据和异常
 */
class ResponseTransformer {

    companion object {
        fun <T> handleResult(): ObservableTransformer<ResponseBean<T>, T> {
            return MyObservableTransformer()
        }
    }

    class MyObservableTransformer<T> : ObservableTransformer<ResponseBean<T>, T> {
        override fun apply(upstream: Observable<ResponseBean<T>>): ObservableSource<T> {
            return upstream.
                    onErrorResumeNext(ErrorResumeFunction())
                    .flatMap(ResponseFunction())
        }
    }

    /**
     * 非服务器产生的异常，比如解析错误，网络连接错误
     */
    class ErrorResumeFunction<T> : Function<Throwable, ObservableSource<out ResponseBean<T>>> {
        override fun apply(t: Throwable): ObservableSource<out ResponseBean<T>> {
            return Observable.error(CustomException.handleException(t))
        }
    }

    /**
     * 服务器产生的错误：HTTP错误代码
     */
    class ResponseFunction<T> : Function<ResponseBean<T>, ObservableSource<T>> {
        override fun apply(t: ResponseBean<T>): ObservableSource<T> {
            val code: Int = t.errorCode
            return if (code >= 0) {
                Observable.just(t.data)
            } else {
                Observable.error(ApiException(t.errorMsg, null, code))
            }
        }

    }

}
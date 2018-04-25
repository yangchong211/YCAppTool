package com.ns.yc.lifehelper.base.loader;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/18
 * 描    述：将一些重复的操作提出来，放到父类以免Loader 里每个接口都有重复代码
 * 修订历史：
 * ================================================
 */
public class BaseLoader {

    /**
     * 将一些重复的操作提出来，放到父类以免Loader 里每个接口都有重复代码
     * @param observable                Observable对象
     * @param <T>                       支持泛型
     * @return                          <T>
     */
    protected  <T> Observable<T> observe(Observable<T> observable){
        return observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 统一线程处理
     * @param <T>                       泛型
     * @return                          <T>
     */
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}

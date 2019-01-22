package com.ycbjie.library.utils.rxUtils;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import java.lang.reflect.Field;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by PC on 2017/9/18.
 * 作者：PC
 */

public class RxUtil {

    public static <T> Observable.Transformer<T, T> rxCacheListHelper(final String key) {
        Observable.Transformer<T, T> list = new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        //指定doOnNext执行线程是新线程
                        .subscribeOn(Schedulers.io())
                        .doOnNext(new Action1<T>() {
                            @Override
                            public void call(final T data) {
                                Schedulers.io().createWorker().schedule(new Action0() {
                                    @Override
                                    public void call() {
                                        LogUtils.d("get data from network finish ,start cache...");
                                        //通过反射获取List,再判空决定是否缓存
                                        if (data == null) {
                                            return;
                                        }
                                        Class clazz = data.getClass();
                                        Field[] fields = clazz.getFields();
                                        for (Field field : fields) {
                                            String className = field.getType().getSimpleName();
                                            // 得到属性值
                                            if (className.equalsIgnoreCase("List")) {
                                                try {
                                                    List list = (List) field.get(data);
                                                    LogUtils.d("list==" + list);
                                                    if (!list.isEmpty()) {
                                                        LogUtils.d("cache finish");
                                                    }
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
        return list;
    }


    public static <T> Observable rxCreateDiskObservable(final String key, final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                LogUtils.d("get data from disk: key==" + key);
                subscriber.onCompleted();}})
                .map(new Func1<String, T>() {
                    @Override
                    public T call(String s) {
                        return new Gson().fromJson(s, clazz);
                    }
                })
                .subscribeOn(Schedulers.io());
    }



    public static <T> Observable.Transformer<T, T> rxCacheBeanHelper(final String key) {
        Observable.Transformer<T, T> transformer = new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        //指定doOnNext执行线程是新线程
                        .subscribeOn(Schedulers.io())
                        .doOnNext(new Action1<T>() {
                            @Override
                            public void call(final T data) {
                                Schedulers.io().createWorker().schedule(new Action0() {
                                    @Override
                                    public void call() {
                                        LogUtils.d("get data from network finish ,start cache...");
                                        LogUtils.d("cache finish");
                                    }
                                });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
        return transformer;
    }




    /**------------------------------------知乎日报-------------------------------------------------------*/

    /**
     * 统一线程处理
     * @param <T>       泛型
     * @return
     */
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 统一返回结果处理
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> handleGoldResult() {   //compose判断结果
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> httpResponseObservable) {
                return httpResponseObservable.flatMap(new Func1<T, Observable<T>>() {
                    @Override
                    public Observable<T> call(T tGoldHttpResponse) {
                        if(tGoldHttpResponse != null) {
                            return createData(tGoldHttpResponse);
                        } else {
                            return Observable.error(new ApiException("服务器返回error"));
                        }
                    }
                });
            }
        };
    }

    /**
     * 统一返回结果处理
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> handleMyResult() {
        //compose判断结果
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> httpResponseObservable) {
                return httpResponseObservable.flatMap(new Func1<T, Observable<T>>() {
                    @Override
                    public Observable<T> call(T tMyHttpResponse) {
                        if(tMyHttpResponse !=null) {
                            return createData(tMyHttpResponse);
                        } else {
                            return Observable.error(new ApiException("服务器返回error"));
                        }
                    }
                });
            }
        };
    }


    /**
     * 生成Observable
     * @param <T>
     * @return
     */
    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private static class ApiException extends Exception{
        ApiException(String msg) {
            super(msg);
        }
    }

}

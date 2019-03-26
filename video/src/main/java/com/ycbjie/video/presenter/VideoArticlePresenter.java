package com.ycbjie.video.presenter;


import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.library.http.JsonUtils;
import com.ycbjie.video.api.VideoModel;
import com.ycbjie.video.contract.VideoArticleContract;
import com.ycbjie.video.model.MultiNewsArticleBean;
import com.ycbjie.video.model.MultiNewsArticleDataBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : VideoArticlePresenter
 *     revise:
 * </pre>
 */
public class VideoArticlePresenter implements VideoArticleContract.Presenter {

    private VideoArticleContract.View mView;
    private String time;
    private String category;
    private List<MultiNewsArticleDataBean> dataList = new ArrayList<>();
    private final CompositeDisposable compositeDisposable;

    public VideoArticlePresenter(VideoArticleContract.View homeView) {
        this.mView = homeView;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        this.time = TimeUtils.getNowString();
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.dispose();
    }

    @Override
    public void doRefresh() {
        if (dataList.size() != 0) {
            dataList.clear();
            time = TimeUtils.getNowString();
        }
        getVideoData();
    }

    @Override
    public void doLoadMoreData() {
        getVideoData();
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    @Override
    public void getVideoData(String... category) {
        try {
            if (null == this.category) {
                this.category = category[0];
            }
        } catch (Exception e) {
            LogUtils.e(e.getLocalizedMessage());
        }

        // 释放内存
        if (dataList.size() > 100) {
            dataList.clear();
        }
        VideoModel model = VideoModel.getInstance();
        Disposable subscribe = model.getVideoArticle(this.category, time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .switchMap(new Function<MultiNewsArticleBean, Observable<MultiNewsArticleDataBean>>() {
                    @Override
                    public Observable<MultiNewsArticleDataBean> apply(@NonNull MultiNewsArticleBean multiNewsArticleBean) throws Exception {
                        List<MultiNewsArticleDataBean> dataList = new ArrayList<>();
                        for (MultiNewsArticleBean.DataBean dataBean : multiNewsArticleBean.getData()) {
                            dataList.add(JsonUtils.getGson().fromJson(dataBean.getContent(), MultiNewsArticleDataBean.class));
                        }
                        return Observable.fromIterable(dataList);
                    }
                })
                .filter(new Predicate<MultiNewsArticleDataBean>() {
                    @Override
                    public boolean test(@NonNull MultiNewsArticleDataBean dataBean) throws Exception {
                        time = dataBean.getBehot_time();
                        if (TextUtils.isEmpty(dataBean.getSource())) {
                            return false;
                        }
                        try {
                            // 过滤头条问答新闻
                            if (dataBean.getSource().contains("头条问答")
                                    || dataBean.getTag().contains("ad")
                                    || dataBean.getSource().contains("话题")) {
                                return false;
                            }
                        } catch (NullPointerException e) {
                            LogUtils.e(e.getLocalizedMessage());
                        }
                        // 过滤重复新闻(与上次刷新的数据比较)
                        for (MultiNewsArticleDataBean bean : dataList) {
                            if (bean.getTitle().equals(dataBean.getTitle())) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .toList()
                .subscribe(new Consumer<List<MultiNewsArticleDataBean>>() {
                    @Override
                    public void accept(@NonNull List<MultiNewsArticleDataBean> list) throws Exception {
                        if (list != null && list.size() > 0) {
                            dataList.addAll(list);
                            mView.setDataView(dataList);
                            mView.showRecyclerView();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        mView.showErrorView();
                        ExceptionUtils.handleException(e);
                    }
                });
        compositeDisposable.add(subscribe);
    }
}

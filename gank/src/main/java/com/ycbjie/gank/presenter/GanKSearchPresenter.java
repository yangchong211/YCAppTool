package com.ycbjie.gank.presenter;

import android.graphics.Color;
import android.text.TextUtils;

import com.ycbjie.gank.api.GanKModel;
import com.ycbjie.gank.bean.bean.SearchResult;
import com.ycbjie.gank.contract.GanKSearchContract;
import com.ycbjie.library.http.ExceptionUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/14
 *     desc  : 干货集中营
 *     revise:
 * </pre>
 */
public class GanKSearchPresenter implements GanKSearchContract.Presenter {

    private GanKSearchContract.View mView;
    private CompositeSubscription mSubscriptions;


    public GanKSearchPresenter(GanKSearchContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        mView.setEditTextCursorColor(Color.WHITE);
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    /**
     * 开始搜索
     */
    @Override
    public void search(String searchText, boolean isLoadMore) {
        if (TextUtils.isEmpty(searchText)) {
            mView.showTip("搜索内容不能为空。");
            return;
        }
        mView.showSearchResult();
        startSearch(searchText,isLoadMore);
    }

    /**
     * 开始删除
     */
    @Override
    public void deleteAllHistory() {

    }

    private int mPage = 1;
    private void startSearch(String searchText, final boolean isLoadMore) {
        if (!isLoadMore) {
            mPage = 1;
            mView.setLoading();
        } else {
            mPage += 1;
        }
        GanKModel model = GanKModel.getInstance();
        model.getSearchResult(searchText, 10, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SearchResult searchResult) {
                        if (!isLoadMore) {
                            if (searchResult == null || searchResult.count == 0) {
                                mView.showTip("没有搜索到结果");
                                mView.hideLoading();
                                mView.showSearchHistory();
                                mView.setEmpty();
                                return;
                            }
                            mView.setSearchItems(searchResult);
                            mView.showSearchResult();
                        } else {
                            mView.addSearchItems(searchResult);
                            mView.showSearchResult();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showTip("搜索出错了。");
                        mView.hideLoading();
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}

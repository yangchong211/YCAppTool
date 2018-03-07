package com.ns.yc.lifehelper.ui.other.gank.presenter;

import android.graphics.Color;
import android.text.TextUtils;

import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.ui.other.gank.bean.SearchHistory;
import com.ns.yc.lifehelper.ui.other.gank.bean.SearchResult;
import com.ns.yc.lifehelper.ui.other.gank.contract.GanKSearchContract;
import com.ns.yc.lifehelper.api.http.gank.GanKModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：干货集中营
 * 修订历史：
 * ================================================
 */
public class GanKSearchPresenter implements GanKSearchContract.Presenter {

    private GanKSearchContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Realm realm;
    private RealmResults<SearchHistory> searchHistories;


    public GanKSearchPresenter(GanKSearchContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        mView.setEditTextCursorColor(Color.WHITE);
        initRealm();
    }

    private void initRealm() {
        if(realm==null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    /**
     * 查找搜索历史记录
     */
    @Override
    public void queryHistory() {
        initRealm();
        if(realm!=null && realm.where(SearchHistory.class).findAll()!=null){
            searchHistories = realm.where(SearchHistory.class).findAll();
        }else {
            return;
        }
        List<SearchHistory> list = new ArrayList<>();
        for(int a=0 ; a<searchHistories.size() ; a++){
            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setContent(searchHistories.get(a).getContent());
            searchHistory.setCreateTimeMill(searchHistories.get(a).getCreateTimeMill());
            list.add(searchHistory);
        }
        // 将查询结果转为list对象
        if (list.size() < 1) {
            mView.showSearchResult();
        } else {
            mView.setHistory(list);
        }
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
        saveOneHistory(searchText);
        startSearch(searchText,isLoadMore);
    }

    /**
     * 开始删除
     */
    @Override
    public void deleteAllHistory() {

    }


    /**
     * 保存搜索数据
     */
    private void saveOneHistory(String historyContent) {
        if (TextUtils.isEmpty(historyContent)) {
            return;
        }
        if(realm!=null && realm.where(SearchHistory.class).findAll()!=null){
            searchHistories = realm.where(SearchHistory.class).findAll();
        }else {
            return;
        }
        realm.beginTransaction();
        SearchHistory searchHistory = realm.createObject(SearchHistory.class);
        searchHistory.setContent(historyContent);
        searchHistory.setCreateTimeMill(System.currentTimeMillis());
        realm.copyToRealm(searchHistory);
        realm.commitTransaction();
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
        Subscription subscription = model.getSearchResult(searchText, 10, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showTip("搜索出错了。");
                        mView.hideLoading();
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
                });
        mSubscriptions.add(subscription);
    }

}

package com.ycbjie.todo.contract;


import android.content.Intent;

import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;
import com.ycbjie.library.db.cache.CacheTaskDetailEntity;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：干货集中营首页桥梁
 * 修订历史：
 * ================================================
 */
public interface SearchListContract {

    interface View extends BaseView {
        Intent intent();
        void hideNoResults();
        void updateToolbarTitle(String title);
        void showNoResults();
        void updateList(List<CacheTaskDetailEntity> list);
        void finishActivity();
        void startActivityAndForResult(Intent intent, int code);
    }

    interface Presenter extends BasePresenter {
        void bindView(SearchListContract.View homeView);
        void onItemClick(int position, CacheTaskDetailEntity entity);
    }


}

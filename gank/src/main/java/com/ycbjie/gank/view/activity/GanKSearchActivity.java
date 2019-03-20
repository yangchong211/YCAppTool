package com.ycbjie.gank.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ycbjie.gank.R;
import com.ycbjie.gank.bean.bean.SearchResult;
import com.ycbjie.gank.bean.cache.CacheSearchHistory;
import com.ycbjie.gank.contract.GanKSearchContract;
import com.ycbjie.gank.presenter.GanKSearchPresenter;
import com.ycbjie.gank.view.adapter.GanKSearchHisAdapter;
import com.ycbjie.gank.view.adapter.GanKSearchListAdapter;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.utils.MDTintUtil;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/3/28
 *     desc  : 干货集中营搜索
 *     revise:
 * </pre>
 */
public class GanKSearchActivity extends BaseActivity implements GanKSearchContract.View, View.OnClickListener {

    private AppBarLayout appbarSearch;
    private Toolbar toolbarSearch;
    private AppCompatEditText edSearch;
    private AppCompatImageView ivEditClear;
    private AppCompatImageView ivSearch;
    private LinearLayout llSearchHistory;
    private TextView tvSearchClean;
    private RecyclerView recyclerSearchHistory;
    private YCRefreshView recyclerView;

    private GanKSearchContract.Presenter presenter = new GanKSearchPresenter(this);
    private GanKSearchHisAdapter mHistoryListAdapter;
    private List<CacheSearchHistory> his = new ArrayList<>();
    private GanKSearchListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_gank_search;
    }

    @Override
    public void initView() {


        appbarSearch = (AppBarLayout) findViewById(R.id.appbar_search);
        toolbarSearch = (Toolbar) findViewById(R.id.toolbar_search);
        edSearch = (AppCompatEditText) findViewById(R.id.ed_search);
        ivEditClear = (AppCompatImageView) findViewById(R.id.iv_edit_clear);
        ivSearch = (AppCompatImageView) findViewById(R.id.iv_search);
        llSearchHistory = (LinearLayout) findViewById(R.id.ll_search_history);
        tvSearchClean = (TextView) findViewById(R.id.tv_search_clean);
        recyclerSearchHistory = (RecyclerView) findViewById(R.id.recycler_search_history);
        recyclerView = (YCRefreshView) findViewById(R.id.recyclerView);

        initToolBar();
        initHisRecycleView();
        initRecycleView();
    }


    @Override
    public void initListener() {
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    ivEditClear.setVisibility(View.VISIBLE);
                } else {
                    ivEditClear.setVisibility(View.GONE);
                    presenter.unSubscribe();
                    recyclerView.showEmpty();
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    llSearchHistory.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
            }
            return false;
        });
        ivEditClear.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        tvSearchClean.setOnClickListener(this);
    }


    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_edit_clear) {
            recyclerView.showEmpty();
            edSearch.setText("");
            KeyboardUtils.showSoftInput(this);
            recyclerView.setVisibility(View.GONE);
            llSearchHistory.setVisibility(View.VISIBLE);
            presenter.unSubscribe();
        } else if (i == R.id.iv_search) {
            search();
        } else if (i == R.id.tv_search_clean) {
            presenter.deleteAllHistory();
        }
    }

    private void initToolBar() {
        StateAppBar.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorTheme));
        setSupportActionBar(toolbarSearch);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbarSearch.setNavigationOnClickListener(view -> finish());
        llSearchHistory.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        ivEditClear.setVisibility(View.GONE);
    }

    /**
     * 开始搜索
     */
    private void search() {
        KeyboardUtils.hideSoftInput(this);
        presenter.search(Objects.requireNonNull(
                edSearch.getText()).toString().trim(), false);
    }

    /**
     * 初始化搜索历史内容list的RecycleView
     */
    private void initHisRecycleView() {
        recyclerSearchHistory.setLayoutManager(new LinearLayoutManager(this));
        mHistoryListAdapter = new GanKSearchHisAdapter(this , his);
        final RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#e5e5e5"));
        recyclerSearchHistory.addItemDecoration(line);
        recyclerSearchHistory.setAdapter(mHistoryListAdapter);
        mHistoryListAdapter.setOnItemClickListener((view, position) -> {
            if(his.size()>position && position>=0){
                KeyboardUtils.hideSoftInput(GanKSearchActivity.this);
                edSearch.setText(his.get(position).getContent());
                edSearch.setSelection(Objects.requireNonNull(
                        edSearch.getText()).toString().length());
                presenter.search(his.get(position).getContent(), false);
            }
        });
    }


    /**
     * 初始化搜索内容list的RecycleView
     */
    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GanKSearchListAdapter(this);
        final RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            if(adapter.getAllData().size()>position && position>=0){
                SearchResult.ResultsBean resultsBean = adapter.getAllData().get(position);
                if ("福利".equals(resultsBean.type)) {
                    Log.e("福利",resultsBean.desc);
                    ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_GALLERY_ACTIVITY);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.URL,resultsBean.url);
                    bundle.putString(Constant.TITLE,resultsBean.desc);
                    ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle);
                }
            }
        });

        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0 && !TextUtils.isEmpty(edSearch.getText())) {
                        presenter.search(edSearch.getText().toString().trim(), true);
                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    adapter.pauseMore();
                    ToastUtils.showShort("网络不可用");
                }
            }

            @Override
            public void onMoreClick() {

            }
        });

        //设置没有数据
        adapter.setNoMore(R.layout.view_recycle_no_more, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtils.showShort("网络不可用");
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtils.showShort("网络不可用");
                }
            }
        });

        //设置错误
        adapter.setError(R.layout.view_recycle_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });

        //刷新
        recyclerView.setRefreshListener(() -> {
            if (NetworkUtils.isConnected()) {
                presenter.search(Objects.requireNonNull(edSearch.getText())
                        .toString().trim(), false);
            } else {
                recyclerView.setRefreshing(false);
                ToastUtils.showShort("网络不可用");
            }
        });
    }

    /**
     * 设置指针颜色
     */
    @Override
    public void setEditTextCursorColor(int cursorColor) {
        MDTintUtil.setCursorTint(edSearch, cursorColor);
    }

    /**
     * 展示搜索结果
     */
    @Override
    public void showSearchResult() {
        llSearchHistory.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.showRecycler();
    }

    /**
     * 展示搜索历史记录
     */
    @Override
    public void showSearchHistory() {
        llSearchHistory.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    /**
     * 设置搜索历史
     */
    @Override
    public void setHistory(List<CacheSearchHistory> history) {
        showSearchHistory();
        his.clear();
        his.addAll(history);
        if(mHistoryListAdapter!=null){
            mHistoryListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 展示消息
     */
    @Override
    public void showTip(String msg) {

    }


    @Override
    public void hideLoading() {
        recyclerView.setRefreshing(false);
    }

    @Override
    public void setEmpty() {
        recyclerView.showEmpty();
    }

    @Override
    public void setLoading() {
        recyclerView.showProgress();
    }

    @Override
    public void setSearchItems(SearchResult searchResult) {
        adapter.clear();
        adapter.addAll(searchResult.results);
        adapter.notifyDataSetChanged();
        recyclerView.setRefreshing(false);
    }

    @Override
    public void addSearchItems(SearchResult searchResult) {
        int start = adapter.getItemCount();
        adapter.addAll(searchResult.results);
        adapter.notifyItemRangeInserted(start, searchResult.results.size());
    }

}

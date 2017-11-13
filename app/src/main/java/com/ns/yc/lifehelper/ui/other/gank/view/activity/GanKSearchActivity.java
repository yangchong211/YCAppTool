package com.ns.yc.lifehelper.ui.other.gank.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.listener.OnListItemClickListener;
import com.ns.yc.lifehelper.ui.other.gank.bean.GanKFavorite;
import com.ns.yc.lifehelper.ui.other.gank.bean.SearchHistory;
import com.ns.yc.lifehelper.ui.other.gank.bean.SearchResult;
import com.ns.yc.lifehelper.ui.other.gank.contract.GanKSearchContract;
import com.ns.yc.lifehelper.ui.other.gank.presenter.GanKSearchPresenter;
import com.ns.yc.lifehelper.ui.other.gank.view.adapter.GanKSearchHisAdapter;
import com.ns.yc.lifehelper.ui.other.gank.view.adapter.GanKSearchListAdapter;
import com.ns.yc.lifehelper.ui.weight.itemLine.RecycleViewItemLine;
import com.ns.yc.lifehelper.utils.MDTintUtil;
import com.ns.yc.lifehelper.utils.statusbar.GanKStatusBarUtil;
import com.pedaily.yc.ycdialoglib.toast.CustomToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/10/14
 * 描    述：干货集中营搜索
 * 修订历史：
 * ================================================
 */
public class GanKSearchActivity extends BaseActivity implements GanKSearchContract.View, View.OnClickListener {

    @Bind(R.id.ed_search)
    AppCompatEditText edSearch;
    @Bind(R.id.iv_edit_clear)
    AppCompatImageView ivEditClear;
    @Bind(R.id.iv_search)
    AppCompatImageView ivSearch;
    @Bind(R.id.toolbar_search)
    Toolbar toolbarSearch;
    @Bind(R.id.appbar_search)
    AppBarLayout appbarSearch;
    @Bind(R.id.tv_search_clean)
    TextView tvSearchClean;
    @Bind(R.id.recycler_search_history)
    RecyclerView recyclerSearchHistory;
    @Bind(R.id.ll_search_history)
    LinearLayout llSearchHistory;
    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;

    private GanKSearchContract.Presenter presenter = new GanKSearchPresenter(this);
    private GanKSearchHisAdapter mHistoryListAdapter;
    private List<SearchHistory> his = new ArrayList<>();
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
                    presenter.queryHistory();
                }
            }
        });
        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                }
                return false;
            }
        });
        ivEditClear.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        tvSearchClean.setOnClickListener(this);
    }


    @Override
    public void initData() {
        presenter.queryHistory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_edit_clear:
                recyclerView.showEmpty();
                edSearch.setText("");
                KeyboardUtils.showSoftInput(this);
                recyclerView.setVisibility(View.GONE);
                llSearchHistory.setVisibility(View.VISIBLE);
                presenter.unSubscribe();
                presenter.queryHistory();
                break;
            case R.id.iv_search:
                search();
                break;
            case R.id.tv_search_clean:
                presenter.deleteAllHistory();
                break;
        }
    }

    private void initToolBar() {
        GanKStatusBarUtil.immersive(this);
        GanKStatusBarUtil.setPaddingSmart(this, toolbarSearch);
        setSupportActionBar(toolbarSearch);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbarSearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        llSearchHistory.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        ivEditClear.setVisibility(View.GONE);
    }

    /**
     * 开始搜索
     */
    private void search() {
        KeyboardUtils.hideSoftInput(this);
        presenter.search(edSearch.getText().toString().trim(), false);
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
        mHistoryListAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(his.size()>position && position>=0){
                    KeyboardUtils.hideSoftInput(GanKSearchActivity.this);
                    edSearch.setText(his.get(position).getContent());
                    edSearch.setSelection(edSearch.getText().toString().length());
                    presenter.search(his.get(position).getContent(), false);
                }
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
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(adapter.getAllData().size()>position && position>=0){
                    SearchResult.ResultsBean resultsBean = adapter.getAllData().get(position);
                    Intent intent = new Intent();
                    if ("福利".equals(resultsBean.type)) {
                        Log.e("福利",resultsBean.desc);
                        /*intent.setClass(GanKSearchActivity.this, BigimgActivity.class);
                        intent.putExtra("title", resultsBean.desc);
                        intent.putExtra("url", resultsBean.url);*/
                    } else {
                        intent.setClass(GanKSearchActivity.this, GanKWebActivity.class);
                        intent.putExtra("title", resultsBean.desc);
                        intent.putExtra("url", resultsBean.url);
                        GanKFavorite favorite = new GanKFavorite();
                        favorite.setAuthor(resultsBean.who);
                        favorite.setData(resultsBean.publishedAt);
                        favorite.setTitle(resultsBean.desc);
                        favorite.setType(resultsBean.type);
                        favorite.setUrl(resultsBean.url);
                        favorite.setGankID(resultsBean.ganhuo_id);
                        intent.putExtra("favorite", favorite);
                    }
                    startActivity(intent);
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
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    presenter.search(edSearch.getText().toString().trim(), false);
                } else {
                    recyclerView.setRefreshing(false);
                    ToastUtils.showShort("网络不可用");
                }
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
    public void setHistory(List<SearchHistory> history) {
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
        CustomToast.warning(this,msg).show();
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

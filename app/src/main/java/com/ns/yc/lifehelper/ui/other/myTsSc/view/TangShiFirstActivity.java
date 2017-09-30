package com.ns.yc.lifehelper.ui.other.myTsSc.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.ConstantJsApi;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.base.BaseApplication;
import com.ns.yc.lifehelper.ui.other.myTsSc.adapter.TangShiAdapter;
import com.ns.yc.lifehelper.ui.other.myTsSc.bean.TangShiChapter;
import com.ns.yc.lifehelper.ui.other.myTsSc.cache.CacheTsList;
import com.ns.yc.lifehelper.ui.other.myTsSc.model.TangShiModel;
import com.ns.yc.lifehelper.ui.weight.itemLine.RecycleViewItemLine;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/29
 * 描    述：唐诗页面
 * 修订历史：
 * ================================================
 */
public class TangShiFirstActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    private String search;
    private TangShiFirstActivity activity;
    private TangShiAdapter adapter;
    private Realm realm;
    private RealmResults<CacheTsList> cacheTsLists;
    private boolean isCache = false;

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle_list;
    }

    @Override
    public void initView() {
        activity = TangShiFirstActivity.this;
        initIntentData();
        initRealm();
        initToolBar();
        initRecycleView();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if(intent!=null){
            search = intent.getStringExtra("search");
        }
    }


    private void initRealm() {
        realm = BaseApplication.getInstance().getRealmHelper();
    }

    private void initToolBar() {
        if(TextUtils.isEmpty(search)){
            search = "唐诗";
        }
        toolbarTitle.setText(search);
        llSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(activity,TangShiDetailActivity.class);
                intent.putExtra("id",adapter.getAllData().get(position).getDetailid());
                intent.putExtra("name",adapter.getAllData().get(position).getName());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new Date());
        if(TimeUtils.isToday(date)){
            isCache = true;
            readCache();
        }else {
            recyclerView.showProgress();
            getTangShi();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.ll_search:
                Intent intent = new Intent(this, TangShiSearchActivity.class);
                intent.putExtra("type","ts");
                startActivity(intent);
                break;
        }
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        adapter = new TangShiAdapter(activity);
        recyclerView.setAdapter(adapter);
        AddHeader();
        //刷新
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        getTangShi();
                    } else {
                        recyclerView.setRefreshing(false);
                    }
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String[] ts_tags = {"李白","白居易","杜甫","杜牧","王维","李商隐","柳宗元","孟浩然","刘禹锡"};
    private void AddHeader() {
        final List<String> tags = new ArrayList<>();
        for(int a=0 ; a<ts_tags.length ; a++){
            tags.add(ts_tags[a]);
        }

        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.head_my_ts_type, parent, false);
                final TagFlowLayout tfl_tag = (TagFlowLayout) view.findViewById(R.id.tfl_tag);
                tfl_tag.setAdapter(new TagAdapter<String>(tags) {
                    @Override
                    public View getView(FlowLayout parent, int position, String o) {
                        LayoutInflater from = LayoutInflater.from(activity);
                        TextView tv = (TextView) from.inflate(R.layout.tag_hot, tfl_tag, false);
                        tv.setText(o);
                        return tv;
                    }
                });
                tfl_tag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        ToastUtils.showShortSafe(ts_tags[position]);
                        return true;
                    }
                });
                return view;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
    }


    private void getTangShi() {
        TangShiModel model = TangShiModel.getInstance(this);
        model.getTangShiAhapter(ConstantJsApi.JsAppKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TangShiChapter>() {
                    @Override
                    public void onCompleted() {
                        recyclerView.showRecycler();
                    }

                    @Override
                    public void onError(Throwable e) {
                        isCache = false;
                        readCache();
                    }

                    @Override
                    public void onNext(TangShiChapter tangShiChapter) {
                        if(adapter==null){
                            adapter = new TangShiAdapter(activity);
                        }

                        if(tangShiChapter!=null && tangShiChapter.getResult()!=null && tangShiChapter.getResult().size()>0){
                            recyclerView.showRecycler();
                            adapter.addAll(tangShiChapter.getResult());
                            adapter.notifyDataSetChanged();
                            cacheData(tangShiChapter.getResult());
                        }else {
                            recyclerView.showError();
                            recyclerView.setErrorView(R.layout.view_custom_empty_data);
                        }
                    }
                });
    }


    private void readCache() {
        if(realm!=null && realm.where(CacheTsList.class).findAll()!=null){
            cacheTsLists = realm.where(CacheTsList.class).findAll();
        }else {
            return;
        }
        if(cacheTsLists.size()==0){
            if(isCache){
                recyclerView.showProgress();
                getTangShi();
            }else {
                recyclerView.showError();
                recyclerView.setErrorView(R.layout.view_custom_empty_data);
            }
            return;
        }
        if(adapter==null){
            adapter = new TangShiAdapter(activity);
        }else {
            adapter.getAllData().clear();
        }
        for(int a=0 ; a<cacheTsLists.size() ; a++){
            TangShiChapter.ResultBean resultBean = new TangShiChapter.ResultBean();
            resultBean.setName(cacheTsLists.get(a).getName());
            resultBean.setAuthor(cacheTsLists.get(a).getAuthor());
            resultBean.setDetailid(cacheTsLists.get(a).getDetailid());
            adapter.add(resultBean);
        }
        adapter.notifyDataSetChanged();
    }


    private void cacheData(List<TangShiChapter.ResultBean> result) {
        if(realm!=null && realm.where(CacheTsList.class).findAll()!=null){
            cacheTsLists = realm.where(CacheTsList.class).findAll();
        }else {
            return;
        }
        realm.beginTransaction();
        cacheTsLists.deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        for(int a=0 ; a<result.size() ; a++){
            CacheTsList tsList = realm.createObject(CacheTsList.class);
            tsList.setName(result.get(a).getName());
            tsList.setAuthor(result.get(a).getAuthor());
            tsList.setDetailid(result.get(a).getDetailid());
        }
        realm.commitTransaction();
    }

}

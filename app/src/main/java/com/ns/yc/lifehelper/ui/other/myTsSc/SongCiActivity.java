package com.ns.yc.lifehelper.ui.other.myTsSc;

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
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.ConstantJsApi;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.ui.other.myTsSc.adapter.TangShiAdapter;
import com.ns.yc.lifehelper.ui.other.myTsSc.bean.TangShiChapter;
import com.ns.yc.lifehelper.ui.other.myTsSc.cache.CacheScList;
import com.ns.yc.lifehelper.ui.other.myTsSc.model.TangShiModel;
import com.ns.yc.lifehelper.ui.other.myTsSc.view.SongCiDetailActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.view.TangShiSearchActivity;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
 * 描    述：宋词页面
 * 修订历史：
 * ================================================
 */
public class SongCiActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private String search;
    private TangShiAdapter adapter;
    private Realm realm;
    private RealmResults<CacheScList> cacheScLists;
    private boolean isCache = false;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.base_refresh_recycle_bar;
    }

    @Override
    public void initView() {
        initIntentData();
        initToolBar();
        initRealm();
        initRecycleView();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if(intent!=null){
            search = intent.getStringExtra("search");
        }
    }

    private void initToolBar() {
        if(TextUtils.isEmpty(search)){
            search = "宋词";
        }
        toolbarTitle.setText(search);
        llSearch.setVisibility(View.VISIBLE);
    }

    private void initRealm() {
        if(realm == null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SongCiActivity.this,SongCiDetailActivity.class);
                intent.putExtra("id",adapter.getAllData().get(position).getDetailid());
                intent.putExtra("name",adapter.getAllData().get(position).getName());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String date = sDateFormat.format(new Date());
        if(TimeUtils.isLeapYear(date)){
            isCache = true;
            readCache();
        }else {
            recyclerView.showProgress();
            getSongCi();
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
                intent.putExtra("type","sc");
                startActivity(intent);
                break;
        }
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(SongCiActivity.this));
        final RecycleViewItemLine line = new RecycleViewItemLine(SongCiActivity.this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        adapter = new TangShiAdapter(SongCiActivity.this);
        recyclerView.setAdapter(adapter);
        AddHeader();
        //刷新
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        getSongCi();
                    } else {
                        recyclerView.setRefreshing(false);
                    }
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(SongCiActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String[] ts_tags = {"苏轼","欧阳修","王安石","白居易","温庭筠","李煜","范仲淹","李商隐","李清照","柳宗元","刘禹锡","杜甫"};
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
                        LayoutInflater from = LayoutInflater.from(SongCiActivity.this);
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


    private void getSongCi() {
        TangShiModel model = TangShiModel.getInstance(this);
        model.getSongCiAhapter(ConstantJsApi.JsAppKey)
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
                            adapter = new TangShiAdapter(SongCiActivity.this);
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
        initRealm();
        if(realm !=null && realm.where(CacheScList.class).findAll()!=null){
            cacheScLists = realm.where(CacheScList.class).findAll();
        }else {
            return;
        }
        if(cacheScLists.size()==0){
            if(isCache){
                recyclerView.showProgress();
                getSongCi();
            }else {
                recyclerView.showError();
                recyclerView.setErrorView(R.layout.view_custom_empty_data);
            }
            return;
        }
        if(adapter==null){
            adapter = new TangShiAdapter(SongCiActivity.this);
        }else {
            adapter.getAllData().clear();
        }
        for(int a=0 ; a<cacheScLists.size() ; a++){
            TangShiChapter.ResultBean resultBean = new TangShiChapter.ResultBean();
            resultBean.setName(cacheScLists.get(a).getName());
            resultBean.setAuthor(cacheScLists.get(a).getAuthor());
            resultBean.setDetailid(cacheScLists.get(a).getDetailid());
            adapter.add(resultBean);
        }
        adapter.notifyDataSetChanged();
    }


    private void cacheData(List<TangShiChapter.ResultBean> result) {
        initRealm();
        if(realm !=null && realm.where(CacheScList.class).findAll()!=null){
            cacheScLists = realm.where(CacheScList.class).findAll();
        }else {
            return;
        }
        realm.beginTransaction();
        cacheScLists.deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        for(int a=0 ; a<result.size() ; a++){
            CacheScList tsList = realm.createObject(CacheScList.class);
            tsList.setName(result.get(a).getName());
            tsList.setAuthor(result.get(a).getAuthor());
            tsList.setDetailid(result.get(a).getDetailid());
        }
        realm.commitTransaction();
    }


}

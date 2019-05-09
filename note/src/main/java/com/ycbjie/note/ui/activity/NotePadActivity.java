package com.ycbjie.note.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.note.R;
import com.ycbjie.note.model.bean.NotePadDetail;
import com.ycbjie.note.model.cache.CacheNotePad;
import com.ycbjie.note.ui.adapter.NotePadListAdapter;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：简易记事本
 * 修订历史：
 * ================================================
 */
@Route(path = ARouterConstant.ACTIVITY_NOTE_ACTIVITY)
public class NotePadActivity extends BaseActivity implements View.OnClickListener {

    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    Toolbar toolbar;
    YCRefreshView recyclerView;
    private NotePadListAdapter adapter;
    private RealmResults<CacheNotePad> cacheNotePads;
    private List<NotePadDetail> list = new ArrayList<>();
    private Realm realm;


    @Override
    protected void onResume() {
        super.onResume();
        refreshNoteList();
    }

    /**
     * 刷新数据
     */
    private void refreshNoteList() {
        getRealmData();
    }

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle_bar;
    }

    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        recyclerView = findViewById(R.id.recyclerView);
        initToolBar();
        initRecycleView();
    }

    private void initToolBar() {
        toolbarTitle.setText("超文本记事");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(adapter.getAllData().size()>position && position>=0){
                    NotePadDetail notePadDetail = adapter.getAllData().get(position);
                    Intent intent = new Intent(NotePadActivity.this, NotePadDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notePad", notePadDetail);
                    intent.putExtra("data", bundle);
                    intent.putExtra("id",notePadDetail.getId());
                    startActivity(intent);
                }
            }
        });
        realm = Realm.getDefaultInstance();
        adapter.setOnItemLongClickListener(position -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(NotePadActivity.this);
            builder.setTitle("提示");
            builder.setMessage("确定删除笔记？");
            builder.setCancelable(false);
            builder.setPositiveButton("确定", (dialog, which) -> {
                if(adapter.getAllData().size()>position && position>=0){
                    NotePadDetail notePadDetail = adapter.getAllData().get(position);
                    final RealmResults<CacheNotePad> cacheNotePads = realm.where(CacheNotePad.class).equalTo("id", notePadDetail.getId()).findAll();
                    realm.executeTransaction(realm -> {
                        cacheNotePads.deleteAllFromRealm();
                        refreshNoteList();
                    });
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();
            return true;
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_add) {
            Intent intent = new Intent(NotePadActivity.this, NotePadNewActivity.class);
            //flag 1是编辑旧的，0是新建
            intent.putExtra("flag", 0);
            intent.putExtra("id", adapter.getAllData().size() + 1);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();

        }
    }



    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(NotePadActivity.this));
        adapter = new NotePadListAdapter(NotePadActivity.this);
        recyclerView.setAdapter(adapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(NotePadActivity.this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), NotePadActivity.this.getResources().getColor(R.color.grayLine));
        recyclerView.addItemDecoration(line);
        //刷新
        recyclerView.setRefreshListener(() -> {
            if (NetworkUtils.isConnected()) {
                getRealmData();
            } else {
                recyclerView.setRefreshing(false);
                Toast.makeText(NotePadActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 从数据库中读取数据
     */
    private void getRealmData() {
        Realm realm = Realm.getDefaultInstance();
        if(realm !=null && realm.where(CacheNotePad.class).findAll()!=null){
            cacheNotePads = realm.where(CacheNotePad.class).findAll();
        }else {
            return;
        }
        list.clear();
        for(int a=0 ; a<cacheNotePads.size() ; a++){
            NotePadDetail notePadDetail = new NotePadDetail();
            notePadDetail.setId(cacheNotePads.get(a).getId());
            notePadDetail.setTitle(cacheNotePads.get(a).getTitle());
            notePadDetail.setContent(cacheNotePads.get(a).getContent());
            notePadDetail.setGroupId(cacheNotePads.get(a).getGroupId());
            notePadDetail.setGroupName(cacheNotePads.get(a).getGroupName());
            notePadDetail.setType(cacheNotePads.get(a).getType());
            notePadDetail.setBgColor(cacheNotePads.get(a).getBgColor());
            notePadDetail.setIsEncrypt(cacheNotePads.get(a).getIsEncrypt());
            notePadDetail.setCreateTime(cacheNotePads.get(a).getCreateTime());
            list.add(notePadDetail);
        }
        if(adapter==null){
            adapter = new NotePadListAdapter(NotePadActivity.this);
        }
        if(list.size()==0){
            recyclerView.showEmpty();
            return;
        }
        adapter.clear();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }


}

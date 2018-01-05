package com.ns.yc.lifehelper.ui.other.myNote;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.cache.CacheNoteDetail;
import com.ns.yc.lifehelper.ui.other.myNote.adapter.NotebookAdapter;
import com.ns.yc.lifehelper.ui.other.myNote.bean.NoteDetail;
import com.ns.yc.lifehelper.ui.other.myNote.view.NoteAddActivity;
import com.ns.yc.lifehelper.ui.weight.noteView.HTQDragGridView;
import com.ns.yc.lifehelper.utils.animation.AnimationsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/12
 * 描    述：我的笔记本页面
 * 修订历史：
 * ================================================
 */
public class NoteActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.gridView)
    HTQDragGridView gridView;
    @Bind(R.id.refresher)
    SwipeRefreshLayout refresher;
    @Bind(R.id.iv_note_trash)
    ImageView ivNoteTrash;
    @Bind(R.id.fab)
    ImageView fab;
    private Realm realm;
    private RealmResults<CacheNoteDetail> cacheNoteDetails;
    private List<NoteDetail> notes = new ArrayList<>();
    private NotebookAdapter adapter;

    public static final int STATE_NONE = 0;             //默认状态
    public static final int STATE_REFRESH = 1;          //开始刷新
    public static final int STATE_LOADMORE = 2;         //刷新中
    public static final int STATE_NOMORE = 3;           //正常
    public static final int STATE_PRESS_NONE = 4;        //正在下拉但还没有到刷新的状态
    public static int mState = STATE_NONE;              //状态

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_note_main;
    }

    @Override
    public void initView() {
        initToolBar();
        initRealm();
        initNoteData();
        initGridView();
        initRefreshView();
    }

    private void initRealm() {
        if(realm == null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }

    private void initToolBar() {
        toolbarTitle.setText("我的笔记本");
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
        fab.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.fab:
                Intent intent = new Intent(this, NoteAddActivity.class);
                intent.putExtra("from","1");
                intent.putExtra("id",0);            //id为0时，为新建
                startActivityForResult(intent,100);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu_mine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.note_tran:
                //ToastUtils.showShortSafe("透明效果");
                gridView.setAlpha(0.55f);
                break;
            case R.id.note_yuan:
                //ToastUtils.showShortSafe("透明效果");
                gridView.setAlpha(0f);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(requestCode==100){
                String fromType = data.getStringExtra("fromType");
                String content = data.getStringExtra("content");
                String time = data.getStringExtra("time");
                String id = data.getStringExtra("id");

                if(Integer.valueOf(fromType)==1){           //新建
                    addRealmData(content,time,id,1);
                }else if(Integer.valueOf(fromType)==2){     //更新
                    addRealmData(content,time,id,2);
                }
            }
        }
    }


    /**
     * 添加到数据库       1.如果是新笔记就添加，2.如果是旧笔记就替换更新
     * @param content
     * @param time
     * @param id
     */
    private void addRealmData(String content, String time, String id , int type) {
        initRealm();
        if(realm.where(CacheNoteDetail.class).findAll()!=null){
            cacheNoteDetails = realm.where(CacheNoteDetail.class).findAll();
        }else {
            return;
        }
        if(type==1){
            realm.beginTransaction();
            CacheNoteDetail cacheNoteDetail = new CacheNoteDetail();
            cacheNoteDetail.setContent(content);
            cacheNoteDetail.setId(Integer.parseInt(id));
            cacheNoteDetail.setTime(time);
            realm.copyToRealm(cacheNoteDetail);
            realm.commitTransaction();
        }else if(type==2){
            for(int a=0 ; a<cacheNoteDetails.size() ; a++){
                //如果是同一笔记，则更新【先删除后添加】
                if(cacheNoteDetails.get(a).getId() == Integer.valueOf(id)){
                    realm.beginTransaction();
                    cacheNoteDetails.deleteFromRealm(a);
                    realm.commitTransaction();
                }
            }
            realm.beginTransaction();
            CacheNoteDetail cacheNoteDetail = new CacheNoteDetail();
            cacheNoteDetail.setContent(content);
            cacheNoteDetail.setId(Integer.parseInt(id));
            cacheNoteDetail.setTime(time);
            realm.copyToRealm(cacheNoteDetail);
            realm.commitTransaction();
        }

        initNoteData();
    }


    /**
     * 读取数据库中的数据
     */
    private void initNoteData() {
        initRealm();
        if(realm!=null && realm.where(CacheNoteDetail.class).findAll()!=null){
            cacheNoteDetails = realm.where(CacheNoteDetail.class).findAll();
        }else {
            return;
        }
        notes.clear();
        for(int a=0 ; a<cacheNoteDetails.size() ; a++){
            NoteDetail noteDetail = new NoteDetail();
            noteDetail.setTime(cacheNoteDetails.get(a).getTime());
            noteDetail.setContent(cacheNoteDetails.get(a).getContent());
            notes.add(noteDetail);
        }
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    private void initGridView() {
        adapter = new NotebookAdapter(this, notes);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NoteActivity.this,NoteAddActivity.class);
                intent.putExtra("id",notes.get(position).getId()+"");
                intent.putExtra("from","2");
                intent.putExtra("time",notes.get(position).getTime());
                intent.putExtra("content",notes.get(position).getContent());
                startActivity(intent);
            }
        });
        gridView.setTrashView(ivNoteTrash);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //删除
        gridView.setOnDeleteListener(new HTQDragGridView.OnDeleteListener() {
            @Override
            public void onDelete(final int position) {
                if(cacheNoteDetails!=null && cacheNoteDetails.size()>0){
                    realm.beginTransaction();
                    cacheNoteDetails.deleteFromRealm(position);
                    realm.commitTransaction();
                }
            }
        });
        //移动
        gridView.setOnMoveListener(new HTQDragGridView.OnMoveListener() {
            @Override
            public void startMove() {
                refresher.setEnabled(false);
                ivNoteTrash.startAnimation(AnimationsUtils.getTranslateAnimation(0, 0, ivNoteTrash.getTop(), 0, 500));
                ivNoteTrash.setVisibility(View.VISIBLE);
            }

            @Override
            public void finishMove() {
                ivNoteTrash.setVisibility(View.INVISIBLE);
                ivNoteTrash.startAnimation(AnimationsUtils.getTranslateAnimation(0, 0, 0, ivNoteTrash.getTop(), 500));
                if (adapter.getDataChange()) {
                    initNoteData();
                }
            }

            @Override
            public void cancelMove() {}
        });

    }

    private void initRefreshView() {
        refresher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    mState = STATE_PRESS_NONE;
                    gridView.setDragEnable(false);
                } else {
                    gridView.setDragEnable(true);
                }
                return false;
            }
        });
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mState == STATE_REFRESH) {
                    return;
                }
                // 设置顶部正在刷新
                gridView.setSelection(0);
                setSwipeRefreshLoadingState();
                initNoteData();
                setSwipeRefreshLoadedState();
                Snackbar.make(gridView, "已从服务器端同步数据！", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    /**
     * 设置顶部正在加载的状态
     */
    private void setSwipeRefreshLoadingState() {
        mState = STATE_REFRESH;
        gridView.setDragEnable(false);
        if (refresher != null) {
            refresher.setRefreshing(true);
        }
    }

    /**
     * 设置顶部加载完毕的状态
     */
    private void setSwipeRefreshLoadedState() {
        mState = STATE_NOMORE;
        if (refresher != null) {
            refresher.setRefreshing(false);
            refresher.setEnabled(true);
        }
        gridView.setDragEnable(true);
    }


}

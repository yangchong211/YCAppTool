package com.ns.yc.lifehelper.ui.other.myPicture.view;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewTreeObserver;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.service.PicGroupService;
import com.ns.yc.lifehelper.ui.other.myPicture.adapter.PicBeautifulGroupAdapter;
import com.ns.yc.lifehelper.ui.other.myPicture.bean.PicBeautifulContentBean;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/4
 * 描    述：美图欣赏组
 * 修订历史：
 * ================================================
 */
public class BeautifulGroupFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private String groupId;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refresher)
    SwipeRefreshLayout refresher;
    private StaggeredGridLayoutManager layoutManager;
    private Realm realm;
    private BeautifulGroupActivity activity;
    private PicBeautifulGroupAdapter mAdapter;
    private RealmResults<PicBeautifulContentBean> groupIdResults;
    private int index;
    private boolean isFrist = false;

    public BeautifulGroupFragment(){};

    @SuppressLint("ValidFragment")
    public BeautifulGroupFragment(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        activity.unregisterReceiver(Receiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BeautifulGroupActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public int getContentView() {
        return R.layout.base_refresh_recycle;
    }

    @Override
    public void initView() {
        refresher.setColorSchemeResources(R.color.gray3);
        refresher.setOnRefreshListener(this);
    }

    @Override
    public void initListener() {
        index = activity.getIndex();
        if (index != -1) {
            recyclerView.scrollToPosition(index);
            recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                    recyclerView.requestLayout();
                    return true;
                }
            });
        }
    }

    @Override
    public void initData() {
        SendToLoad();
    }


    @Override
    public void onRefresh() {

    }

    private int currentCount;
    private BroadcastReceiver Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(groupId)) {
                int count = intent.getIntExtra("count", 0);

                if( realm.where(PicBeautifulContentBean.class).findAll()!=null){
                    groupIdResults = realm.where(PicBeautifulContentBean.class)
                            .equalTo("groupid", groupId)
                            .findAll();
                    currentCount = groupIdResults.size();
                }

                refresher.setRefreshing(true);
                if (currentCount == count || count == 0) {
                    refresher.setRefreshing(false);
                }
                if (currentCount == 0) {//数据库空 第一次网络加载
                    isFrist = true;
                }
                if (isFrist) {
                    mAdapter.notifyItemInserted(currentCount);
                }
            }
            mAdapter.replaceWith(groupIdResults);
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRecycleView();


        IntentFilter filter = new IntentFilter();
        filter.addAction(groupId);
        activity.registerReceiver(Receiver, filter);
        initRealm();
        mAdapter = new PicBeautifulGroupAdapter(activity) {
            @Override
            protected void onItemClick(View v, int position) {
                startLargePicActivity(v, position);
            }
        };
        recyclerView.setAdapter(mAdapter);
        SendToLoad();

        //下面这段话是？？？？
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                String newTransitionName = mAdapter.get(index).getUrl();
                View newSharedView = recyclerView.findViewWithTag(newTransitionName);
                if (newSharedView != null) {
                    names.clear();
                    names.add(newTransitionName);
                    sharedElements.clear();
                    sharedElements.put(newTransitionName, newSharedView);
                }
            }
        });
    }

    private void initRealm() {
        realm = Realm.getDefaultInstance();
    }


    private void initRecycleView() {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Picasso.with(getContext()).resumeTag("1");
                } else {
                    Picasso.with(getContext()).pauseTag("1");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] positions = new int[layoutManager.getSpanCount()];
                layoutManager.findLastVisibleItemPositions(positions);
                int position = Math.max(positions[0], positions[1]);
                if (position == layoutManager.getItemCount() - 1) {
                    onRefresh();
                }
            }
        });
    }

    private void startLargePicActivity(View view, int position) {
        Intent intent = new Intent(activity, MyPicLargeActivity.class);
        intent.putExtra("index", position);
        intent.putExtra("groupId", groupId);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, mAdapter.get(position).getUrl());
        activity.startActivity(intent, options.toBundle());
    }


    private void SendToLoad() {
        Intent intent = new Intent(activity, PicGroupService.class);
        intent.putExtra("groupId", groupId);
        getActivity().startService(intent);
    }

}

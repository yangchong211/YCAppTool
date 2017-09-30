package com.ns.yc.lifehelper.ui.other.myPicture.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseApplication;
import com.ns.yc.lifehelper.service.PicBeautifulService;
import com.ns.yc.lifehelper.ui.other.myPicture.adapter.PicBeautifulAdapter;
import com.ns.yc.lifehelper.ui.other.myPicture.bean.PicBeautifulMainBean;
import com.ns.yc.lifehelper.ui.other.myPicture.util.BeautifulPicUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/1
 * 描    述：美图欣赏
 * 修订历史：
 * ================================================
 */
public class MyPicBeautifulFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refresher)
    SwipeRefreshLayout refresher;
    private String mType;
    private static String TYPE = "wx";
    private MyPicBeautifulActivity activity;
    private StaggeredGridLayoutManager layoutManager;
    private int page = 2;
    protected boolean isVisible;
    private boolean hasload;

    private BroadcastReceiver Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mType)) {
                boolean isRefreshe, isLoadmore, isFirstload;
                if( realm.where(PicBeautifulMainBean.class).findAll()!=null){
                    latest = realm.where(PicBeautifulMainBean.class)
                            .equalTo("type", mType)
                            .findAll();
                }
                hasload = false;

                isRefreshe = intent.getBooleanExtra("isRefreshe", false);
                isLoadmore = intent.getBooleanExtra("isLoadmore", true);
                isFirstload = intent.getBooleanExtra("isFirstload", false);

                if (isFirstload || isRefreshe) {
                    mAdapter.replaceWith(latest);
                }
                if (isLoadmore) {
                    //mAdapter.addAll(latest.subList(latest.size() - 24, latest.size()));
                    mAdapter.addAll(latest);
                }
                refresher.setRefreshing(false);
            }
        }
    };
    private Realm realm;
    private PicBeautifulAdapter mAdapter;
    private RealmResults<PicBeautifulMainBean> latest;


    public static MyPicBeautifulFragment newInstance(String param1) {
        MyPicBeautifulFragment fragment = new MyPicBeautifulFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MyPicBeautifulActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
        if (mType == null || mType.length() == 0) {
            mType = "1";
        }
    }

    @Override
    public void onDestroyView() {
        activity.unregisterReceiver(Receiver);
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRecycleView();


        IntentFilter filter = new IntentFilter();
        filter.addAction(mType);
        activity.registerReceiver(Receiver, filter);
        initRealm();
        mAdapter = new PicBeautifulAdapter(activity) {
            @Override
            protected void onItemClick(View v, int position) {
                startGroupActivity(v, position);
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    private void startGroupActivity(View view, int position) {
        Intent intent = new Intent(activity, BeautifulGroupActivity.class);
        intent.putExtra("index", position);
        intent.putExtra("groupId", BeautifulPicUtils.url2GroupId(mAdapter.get(position).getUrl()));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, mAdapter.get(position).getUrl());
        activity.startActivity(intent, options.toBundle());
    }


    /**
     * 懒加载方案，实现加载延迟，这个方法最先走
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    protected void onVisible() {
        if (!hasload) {
            lazyLoad();
            hasload = true;
        }
    }

    protected void onInvisible() {

    }

    private void lazyLoad() {
        /*if (!isVisible) {
            return;
        }
        SendToLoad("");*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_refresh_recycle, container, false);
        ButterKnife.bind(this, view);
        refresher.setColorSchemeResources(R.color.redTab);
        refresher.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SendToLoad("");
    }

    private void initRealm() {
        realm = BaseApplication.getInstance().getRealmHelper();
        //realm = Realm.getDefaultInstance();
        realm.createAllFromJson(PicBeautifulMainBean.class,"");
    }


    //发送去加载首页数据        就是page页数
    private void SendToLoad(String page) {
        if(activity!=null){
            Intent intent = new Intent(activity, PicBeautifulService.class);
            intent.putExtra("type", mType);
            intent.putExtra("page", page);
            activity.startService(intent);
        }
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
                    loadMore();
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        SendToLoad("");
    }


    private void loadMore() {
        if (hasload) {
            return;
        }
        SendToLoad(String.valueOf(page));
        page++;
        hasload = true;
    }


}

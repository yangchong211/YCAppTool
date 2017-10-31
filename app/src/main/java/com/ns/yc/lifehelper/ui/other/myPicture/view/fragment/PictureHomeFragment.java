package com.ns.yc.lifehelper.ui.other.myPicture.view.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.other.myPicture.MyPictureActivity;
import com.ns.yc.lifehelper.ui.other.myPicture.adapter.PictureHomeAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/4
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class PictureHomeFragment extends BaseFragment {


    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    private MyPictureActivity activity;
    private PictureHomeAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MyPictureActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle;
    }

    @Override
    public void initView() {
        initRecycleView();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        adapter.clear();
        recyclerView.showProgress();
        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new GridLayoutManager(activity,2));
        adapter = new PictureHomeAdapter(activity);
        recyclerView.setAdapter(adapter);

        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {

                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    adapter.pauseMore();
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
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

                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getData() {

    }


}

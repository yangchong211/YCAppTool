package com.yc.ycvideoplayer.video.tiktok;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yc.ycvideoplayer.ConstantVideo;

import com.yc.ycvideoplayer.R;
import com.yc.video.config.VideoInfoBean;

import java.util.ArrayList;
import java.util.List;

public class TikTok1ListFragment extends Fragment {

    private List<VideoInfoBean> data = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TikTok1ListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }


    protected void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter = new TikTok1ListAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }


    protected void initData() {
        //模拟请求数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<VideoInfoBean> tiktokBeans = ConstantVideo.getVideoList();
                data.addAll(tiktokBeans);
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}

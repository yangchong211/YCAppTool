package com.ns.yc.lifehelper.ui.other.listener.view;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.weight.RecyclerViewFastScroller;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/9/18
 * 描    述：我的听听，本地所有音乐页面
 * 修订历史：
 * ================================================
 */
public class ListenerAllLocalFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.bubble)
    TextView bubble;
    @Bind(R.id.handle)
    AppCompatImageView handle;
    @Bind(R.id.fast_scroller)
    RecyclerViewFastScroller fastScroller;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public int getContentView() {
        return R.layout.fragment_listener_local;
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

    }

    private void initRecycleView() {

    }

}

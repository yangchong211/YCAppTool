package com.ns.yc.lifehelper.ui.other.myVideo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.weight.itemLine.RecycleViewItemLine;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：本地视频页面
 * 修订历史：
 * ================================================
 */
public class VideoLocalActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_video_local;
    }

    @Override
    public void initView() {
        initToolBar();
        initRecycleView();
    }

    private void initToolBar() {
        toolbarTitle.setText("本地视频");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.px2dp(2), this.getResources().getColor(R.color.grayLine));
        recyclerView.addItemDecoration(line);
    }

}

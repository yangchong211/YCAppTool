package com.ns.yc.lifehelper.ui.other.vtex.view.activity;

import android.content.res.XmlResourceParser;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp.BaseActivity;
import com.ns.yc.lifehelper.ui.other.vtex.view.adapter.WTNodeAdapter;
import com.ns.yc.lifehelper.utils.XmlUtil;

import butterknife.Bind;


public class WTNoteActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv_node_title)
    TextView tvNodeTitle;

    private ArrayMap<String, ArrayMap<String, String>> map;
    WTNodeAdapter mAdapter;
    private LinearLayoutManager mManager;
    private int mTitleHeight;
    private int mCurrentPosition;

    @Override
    public int getContentView() {
        return R.layout.activity_wtex_note;
    }

    @Override
    public void initView() {
        initToolBar();
    }

    private void initToolBar() {
        toolbarTitle.setText("节点导航");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        XmlResourceParser xmlParser = this.getResources().getXml(R.xml.wt_news_nodes);
        try {
            map = XmlUtil.parseNodes(xmlParser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter = new WTNodeAdapter(this , map);
        mManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mTitleHeight = tvNodeTitle.getHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = mManager.findViewByPosition(mCurrentPosition + 1);
                if (view != null) {
                    if (view.getTop() <= mTitleHeight) {
                        tvNodeTitle.setY(-(mTitleHeight - view.getTop()));
                    } else {
                        tvNodeTitle.setY(0);
                    }
                }
                if (mCurrentPosition != mManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = mManager.findFirstVisibleItemPosition();
                    tvNodeTitle.setY(0);
                    if (map != null) {
                        tvNodeTitle.setText(map.keyAt(mCurrentPosition));
                    }
                }
            }
        });
        tvNodeTitle.setText(map.keyAt(0));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


}

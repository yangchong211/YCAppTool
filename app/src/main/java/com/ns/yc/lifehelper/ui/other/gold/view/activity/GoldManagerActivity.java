package com.ns.yc.lifehelper.ui.other.gold.view.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.comment.Constant;
import com.ns.yc.lifehelper.base.state.BaseStateBarActivity;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldManagerBean;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldManagerItemBean;
import com.ns.yc.lifehelper.ui.other.gold.view.adapter.GoldManagerAdapter;
import com.ns.yc.lifehelper.utils.rxUtils.RxBus;
import com.ns.yc.ycstatelib.StateLayoutManager;

import org.yczbj.ycrefreshviewlib.callback.DefaultItemTouchHelpCallback;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.util.Collections;

import butterknife.BindView;
import io.realm.RealmList;


public class GoldManagerActivity extends BaseStateBarActivity implements View.OnClickListener {

    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private RealmList<GoldManagerItemBean> mList;
    private GoldManagerAdapter adapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().post(new GoldManagerBean(mList));
    }

    @Override
    protected void initStatusLayout() {
        statusLayoutManager = StateLayoutManager.newBuilder(this)
                .contentView(R.layout.base_recycler_view)
                .emptyDataView(R.layout.view_custom_empty_data)
                .errorView(R.layout.view_custom_data_error)
                .loadingView(R.layout.view_custom_loading_data)
                .netWorkErrorView(R.layout.view_custom_network_error)
                .build();
    }

    @Override
    public void initView() {
        initToolBar();
        initIntentData();
        statusLayoutManager.showLoading();
    }

    private void initToolBar() {
        toolbarTitle.setText("模块特别展示");
    }

    private void initIntentData() {
        mList = ((GoldManagerBean) getIntent().getParcelableExtra(
                Constant.DetailKeys.IT_GOLD_MANAGER)).getManagerList();
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        adapter = new GoldManagerAdapter(this, mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(10), Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
        DefaultItemTouchHelpCallback callback = new DefaultItemTouchHelpCallback(
                new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
            }

            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
                if (mList != null) {
                    Collections.swap(mList, srcPosition, targetPosition);
                    adapter.notifyItemMoved(srcPosition, targetPosition);
                    return true;
                }
                return false;
            }
        });
        callback.setDragEnable(true);
        callback.setSwipeEnable(false);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        statusLayoutManager.showContent();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            default:
                break;
        }
    }

}

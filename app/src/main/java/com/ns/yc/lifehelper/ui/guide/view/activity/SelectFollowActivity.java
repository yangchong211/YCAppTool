package com.ns.yc.lifehelper.ui.guide.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.bean.SelectPoint;
import com.ns.yc.lifehelper.listener.OnListItemClickListener;
import com.ns.yc.lifehelper.ui.guide.contract.SelectFollowContract;
import com.ns.yc.lifehelper.ui.guide.presenter.SelectFollowPresenter;
import com.ns.yc.lifehelper.ui.guide.view.adapter.SelectFollowAdapter;
import com.ns.yc.lifehelper.ui.main.view.activity.MainActivity;
import com.yc.cn.ycrecycleviewlib.select.SelectRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/5/18
 * 描    述：关注点页面
 * 修订历史：
 * ================================================
 */
public class SelectFollowActivity extends BaseActivity implements SelectFollowContract.View, View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.tv_title_right)
    TextView tvTitleRight;
    @Bind(R.id.select_view)
    SelectRecyclerView selectView;
    @Bind(R.id.tv_clean)
    TextView tvClean;
    @Bind(R.id.tv_start)
    TextView tvStart;

    private SelectFollowContract.Presenter presenter = new SelectFollowPresenter(this);
    private SelectFollowAdapter adapter;
    private List<SelectPoint> lists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_select_follow;
    }

    @Override
    public void initView() {
        initToolBar();
        initRecycleView();
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        tvClean.setOnClickListener(this);
        tvStart.setOnClickListener(this);
        adapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (adapter.data != null && adapter.data.size() > 0) {
                    adapter.toggleSelected(position);
                }
            }
        });
    }

    @Override
    public void initData() {
        presenter.addData(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                presenter.goMainActivity();
                break;
            case R.id.tv_title_right:
                presenter.goMainActivity();
                break;
            case R.id.tv_clean:
                if(adapter!=null && adapter.data!=null){
                    adapter.clearSelected();
                }
                break;
            case R.id.tv_start:
                if(adapter!=null && adapter.data!=null){
                    if(adapter.getSelectedIndices().length>0){
                        presenter.addSelectToRealm(adapter.getSelectedIndices());
                    }
                }
                presenter.goMainActivity();
                break;
        }
    }


    private void initToolBar() {
        toolbarTitle.setText("关注点");
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setText("跳过");
    }


    private void initRecycleView() {
        selectView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new SelectFollowAdapter(this, lists);
        selectView.setAdapter(adapter);
        //下划线
        SpaceDecoration itemDecoration = new SpaceDecoration(SizeUtils.dp2px(5));
        itemDecoration.setPaddingEdgeSide(false);
        itemDecoration.setPaddingStart(false);
        itemDecoration.setPaddingHeaderFooter(false);
        selectView.addItemDecoration(itemDecoration);
    }


    @Override
    public void refreshData(List<SelectPoint> list) {
        lists.clear();
        lists.addAll(list);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void toMainActivity() {
        startActivity(MainActivity.class);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_FIRST_SPLASH, false);
    }

}

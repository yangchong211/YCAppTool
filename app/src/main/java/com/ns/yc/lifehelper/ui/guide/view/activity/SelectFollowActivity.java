package com.ns.yc.lifehelper.ui.guide.view.activity;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.model.bean.SelectPoint;
import com.ns.yc.lifehelper.ui.guide.contract.SelectFollowContract;
import com.ns.yc.lifehelper.ui.guide.presenter.SelectFollowPresenter;
import com.ns.yc.lifehelper.ui.guide.view.adapter.SelectFollowAdapter;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.yc.cn.ycrecycleviewlib.select.SelectRecyclerView;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.constant.Constant;

import org.yczbj.ycrefreshviewlib.item.SpaceViewItemLine;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 关注点页面
 *     revise: https://github.com/yangchong211/YCRecycleView
 * </pre>
 */
public class SelectFollowActivity extends BaseActivity<SelectFollowPresenter>
        implements SelectFollowContract.View, View.OnClickListener {


    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    TextView tvTitleRight;
    SelectRecyclerView selectView;
    TextView tvClean;
    TextView tvStart;

    private SelectFollowContract.Presenter presenter = new SelectFollowPresenter(this);
    private SelectFollowAdapter adapter;
    private List<SelectPoint> lists = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_select_follow;
    }

    @Override
    public void initView() {
        initFindViewById();
        initToolBar();
        initRecycleView();
    }

    private void initFindViewById() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        tvTitleRight = findViewById(R.id.tv_title_right);
        selectView = findViewById(R.id.select_view);
        tvClean = findViewById(R.id.tv_clean);
        tvStart = findViewById(R.id.tv_start);
    }


    private void initToolBar() {
        toolbarTitle.setText("关注点");
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setText("跳过");
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        tvTitleRight.setOnClickListener(this);
        tvClean.setOnClickListener(this);
        tvStart.setOnClickListener(this);
        adapter.setOnItemClickListener((view, position) -> {
            if (adapter.data != null && adapter.data.size() > 0) {
                adapter.toggleSelected(position);
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
                toMainActivity();
                break;
            case R.id.tv_title_right:
                toMainActivity();
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
                toMainActivity();
                break;
            default:
                break;
        }
    }


    private void initRecycleView() {
        selectView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new SelectFollowAdapter(this, lists);
        selectView.setAdapter(adapter);
        //下划线
        SpaceViewItemLine itemDecoration = new SpaceViewItemLine(SizeUtils.dp2px(5));
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
        MainActivity.startActivity(this,MainActivity.FIND);
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_FIRST_SPLASH, false);
        finish();
    }

}

package com.ns.yc.lifehelper.ui.other.myPicture.view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.find.view.adapter.PicNiceAdapter;
import com.ns.yc.lifehelper.ui.find.bean.GanKNicePicBean;
import com.ns.yc.lifehelper.ui.find.model.GanKNicePicModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/1
 * 描    述：图片不规则画廊效果
 * 修订历史：
 * ================================================
 */
public class MyPicNiceActivity extends BaseActivity implements View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.bga_refresh)
    BGARefreshLayout bgaRefresh;
    private PicNiceAdapter adapter;
    private int size = 10;              //默认每次加载10条数据
    private int page = 1;               //默认页码1
    private List<GanKNicePicBean.ResultsBean> gankPic = new ArrayList<>();


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.base_bga_recycle_header;
    }

    @Override
    public void initView() {
        initToolBar();
        initRefresh(bgaRefresh);
        initRecycleView();
    }

    private void initToolBar() {
        toolbarTitle.setText("画廊效果");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getGanKNicePic(size,page);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }

    private void initRefresh(BGARefreshLayout bgaRefresh) {
        // 为BGARefreshLayout设置代理
        bgaRefresh.setDelegate(this);
        //设置下拉刷新不可用
        bgaRefresh.setPullDownRefreshEnable(true);
        bgaRefresh.setIsShowLoadingMoreView(true);
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(MyPicNiceActivity.this, true);
        refreshViewHolder.setRefreshingText("正在加载数据中");
        refreshViewHolder.setReleaseRefreshText("松开立即刷新");
        refreshViewHolder.setPullDownRefreshText("下拉可以刷新");
        bgaRefresh.setRefreshViewHolder(refreshViewHolder);
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if(NetworkUtils.isConnected()){
            size = 10 ;
            page = 1;
            getGanKNicePic(size,page);
        } else {
            ToastUtils.showShortSafe("没有网络");
            refreshLayout.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(NetworkUtils.isConnected()){
            size = 10 ;
            page = page+1;
            getGanKNicePic(size,page);
            return true;
        }else {
            ToastUtils.showShortSafe("没有网络");
            refreshLayout.endLoadingMore();
            return false;
        }
    }

    private void initRecycleView() {
        //recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//不设置的话，图片闪烁错位，有可能有整列错位的情况。
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PicNiceAdapter(MyPicNiceActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.setData(gankPic);
    }


    private void getGanKNicePic(int size, final int page) {
        GanKNicePicModel model = GanKNicePicModel.getInstance(MyPicNiceActivity.this);
        model.getGanKNicePic(String.valueOf(size),String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GanKNicePicBean>() {
                    @Override
                    public void onCompleted() {
                        Log.e("dfa","fdsa");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("dfa","fdsa");
                    }

                    @Override
                    public void onNext(GanKNicePicBean ganKNicePicBean) {
                        if(adapter==null){
                            adapter = new PicNiceAdapter(MyPicNiceActivity.this);
                        }
                        if(page==1){
                            if(ganKNicePicBean!=null){
                                if(ganKNicePicBean.getResults()!=null && ganKNicePicBean.getResults().size()>0){
                                    List<GanKNicePicBean.ResultsBean> results = ganKNicePicBean.getResults();
                                    gankPic.clear();
                                    gankPic.addAll(results);
                                    adapter.setData(gankPic);
                                    bgaRefresh.scrollTo(0,0);
                                }else {
                                    bgaRefresh.endRefreshing();
                                }
                            }else {
                                bgaRefresh.endRefreshing();
                            }
                        } else {
                            if(ganKNicePicBean!=null){
                                if(ganKNicePicBean.getResults()!=null && ganKNicePicBean.getResults().size()>0){
                                    List<GanKNicePicBean.ResultsBean> results = ganKNicePicBean.getResults();
                                    gankPic.addAll(results);
                                    adapter.setData(gankPic);
                                    bgaRefresh.endLoadingMore();
                                }else {
                                    bgaRefresh.endLoadingMore();
                                }
                            }else {
                                bgaRefresh.endLoadingMore();
                            }
                        }
                    }
                });
    }

}

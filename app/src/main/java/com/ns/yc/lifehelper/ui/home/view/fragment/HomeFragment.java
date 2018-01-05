package com.ns.yc.lifehelper.ui.home.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.adapter.BaseBannerPagerAdapter;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.bean.HomeBlogEntity;
import com.ns.yc.lifehelper.ui.home.contract.HomeFragmentContract;
import com.ns.yc.lifehelper.ui.home.presenter.HomeFragmentPresenter;
import com.ns.yc.lifehelper.ui.home.view.adapter.HomeBlogAdapter;
import com.ns.yc.lifehelper.ui.main.view.activity.MainActivity;
import com.ns.yc.lifehelper.ui.main.view.activity.WebViewActivity;
import com.ns.yc.lifehelper.ui.other.myKnowledge.MyKnowledgeActivity;
import com.ns.yc.lifehelper.ui.other.myNews.txNews.TxNewsActivity;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.WxNewsActivity;
import com.ns.yc.lifehelper.ui.other.myNews.wyNews.WyNewsActivity;
import com.ns.yc.lifehelper.ui.weight.MarqueeView;
import com.ns.yc.lifehelper.ui.weight.pileCard.ItemEntity;
import com.ns.yc.lifehelper.utils.DialogUtils;
import com.ns.yc.lifehelper.utils.ImageUtils;
import com.ns.yc.yccardviewlib.CardViewLayout;
import com.yc.cn.ycbannerlib.first.BannerView;
import com.yc.cn.ycbannerlib.first.util.SizeUtil;

import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/3/21
 * 描    述：Home主页面
 * 修订历史：
 *
 *       v1.5 修改于11月3日，改写代码为MVP架构
 * ================================================
 */
public class HomeFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate
        , View.OnClickListener ,HomeFragmentContract.View{

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.bga_refresh)
    BGARefreshLayout bgaRefresh;

    private MainActivity activity;
    private BannerView banner;
    private TextView tv_home_first;
    private TextView tv_home_second;
    private TextView tv_home_third;
    private TextView tv_home_four;
    private View headerView;
    private MarqueeView marqueeView;

    private HomeFragmentContract.Presenter presenter = new HomeFragmentPresenter(this);
    private HomeBlogAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(activity!=null){
            activity = null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(banner!=null){
            banner.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(banner!=null){
            banner.resume();
        }
    }


    @Override
    public int getContentView() {
        return R.layout.base_bga_recycle_list;
    }

    @Override
    public void initView() {
        initRefresh(bgaRefresh);
        initRecycleView();
        initBanner();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.bindView(activity);
        presenter.subscribe();
    }

    @Override
    public void initListener() {
        tv_home_first.setOnClickListener(this);
        tv_home_second.setOnClickListener(this);
        tv_home_third.setOnClickListener(this);
        tv_home_four.setOnClickListener(this);
    }

    @Override
    public void initData() {
        presenter.getHomeNewsData();
    }

    private void initRefresh(BGARefreshLayout bgaRefresh) {
        // 为BGARefreshLayout设置代理
        bgaRefresh.setDelegate(this);
        //设置下拉刷新不可用
        bgaRefresh.setPullDownRefreshEnable(true);
        bgaRefresh.setIsShowLoadingMoreView(true);
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(activity, true);
        refreshViewHolder.setRefreshingText("正在加载数据中");
        refreshViewHolder.setReleaseRefreshText("松开立即刷新");
        refreshViewHolder.setPullDownRefreshText("下拉可以刷新");
        bgaRefresh.setRefreshViewHolder(refreshViewHolder);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if(NetworkUtils.isConnected()){
            presenter.getHomeNewsData();
        } else {
            ToastUtils.showShortSafe("没有网络");
            refreshLayout.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(NetworkUtils.isConnected()){
            presenter.getHomeNewsData();
            return true;
        }else {
            ToastUtils.showShortSafe("没有网络");
            refreshLayout.endLoadingMore();
            return false;
        }
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        adapter = new HomeBlogAdapter(activity, recyclerView);
        headerView = getHeaderView();
        if(headerView!=null){
            adapter.addHeaderView(headerView);
        }
        recyclerView.setAdapter(adapter.getHeaderAndFooterAdapter());
        adapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                if(position>0 && adapter.getData().size()>position){
                    Intent intent = new Intent(activity, WebViewActivity.class);
                    intent.putExtra("url",adapter.getData().get(position).getUrl());
                    startActivity(intent);
                }else if(position==0){
                    startActivity(WxNewsActivity.class);
                }
            }
        });
    }


    private View getHeaderView() {
        View header =  View.inflate(activity, R.layout.head_home_main, null);
        banner = (BannerView) header.findViewById(R.id.banner);
        tv_home_first = (TextView) header.findViewById(R.id.tv_home_first);
        tv_home_second = (TextView) header.findViewById(R.id.tv_home_second);
        tv_home_third = (TextView) header.findViewById(R.id.tv_home_third);
        tv_home_four = (TextView) header.findViewById(R.id.tv_home_four);
        marqueeView = (MarqueeView) header.findViewById(R.id.marqueeView);
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                //存放自己的博客
            }
        });
        CardViewLayout pileLayout = (CardViewLayout) header.findViewById(R.id.pileLayout);
        initMarqueeView();
        initPileCard(pileLayout);
        return header;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_home_first:
                startActivity(WxNewsActivity.class);
                break;
            case R.id.tv_home_second:
                startActivity(WyNewsActivity.class);
                break;
            case R.id.tv_home_third:
                startActivity(TxNewsActivity.class);
                break;
            case R.id.tv_home_four:
                startActivity(MyKnowledgeActivity.class);
                break;
            default:
                break;
        }
    }

    /**初始化轮播图*/
    private void initBanner() {
        if(headerView!=null && banner!=null){
            List<Object> lists = presenter.getBannerData();
            banner.setHintGravity(1);
            banner.setAnimationDuration(1000);
            banner.setPlayDelay(2000);
            banner.setHintPadding(0,0,0, SizeUtil.dip2px(activity,10));
            banner.setAdapter(new BaseBannerPagerAdapter(activity, lists));
        }
    }


    private void initMarqueeView() {
        if(marqueeView==null){
            return;
        }
        List<CharSequence> list = presenter.getMarqueeTitle();
        marqueeView.startWithList(list);
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                switch (position){
                    case 0:
                        DialogUtils.showCustomPopupWindow(activity);
                        break;
                    case 1:
                        Intent intent1 = new Intent(activity,WebViewActivity.class);
                        intent1.putExtra("url","https://github.com/yangchong211");
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(activity,WebViewActivity.class);
                        intent2.putExtra("url","http://www.ximalaya.com/zhubo/71989305/");
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void initPileCard(CardViewLayout pileLayout) {
        if(presenter.getHomePileData()!=null){
            ArrayList<ItemEntity> dataList = presenter.getHomePileData();
            initPile(pileLayout,dataList);
        }
    }


    private void initPile(CardViewLayout pileLayout,final ArrayList<ItemEntity> dataList) {
        pileLayout.setAdapter(new CardViewLayout.Adapter() {

            class ViewHolder {
                ImageView imageView;
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_card_layout;
            }

            @Override
            public void bindView(View view, int position) {
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                if (viewHolder == null) {
                    viewHolder = new ViewHolder();
                    viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
                    view.setTag(viewHolder);
                }
                if(dataList.size()>0){
                    ImageUtils.loadImgByPicassoWithRound(activity,dataList.get(position).getCoverImageUrl()
                            ,SizeUtils.dp2px(15),R.drawable.image_default,viewHolder.imageView);
                }
            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }

            @Override
            public void displaying(int position) {

            }

            @Override
            public void onItemClick(View view, int position) {
                super.onItemClick(view, position);
                ToastUtils.showShortSafe("点击了"+position+"图片");
            }
        });
    }


    @Override
    public void setNewsData(List<HomeBlogEntity> list) {
        if (list != null && list.size() > 0) {
            adapter.clear();
            adapter.addNewData(list);
            bgaRefresh.endRefreshing();
            bgaRefresh.scrollTo(0, 0);
        }
    }
}

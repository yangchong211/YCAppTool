package com.ns.yc.lifehelper.ui.home.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.adapter.BaseBannerPagerAdapter;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.model.bean.HomeBlogEntity;
import com.ns.yc.lifehelper.ui.home.contract.HomeFragmentContract;
import com.ns.yc.lifehelper.ui.home.presenter.HomeFragmentPresenter;
import com.ns.yc.lifehelper.ui.home.view.adapter.HomeBlogAdapter;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.ns.yc.lifehelper.ui.main.view.activity.WebViewActivity;
import com.ns.yc.lifehelper.ui.other.douban.douMovie.view.DouMovieActivity;
import com.ns.yc.lifehelper.ui.other.myKnowledge.MyKnowledgeActivity;
import com.ns.yc.lifehelper.ui.other.myNews.txNews.TxNewsActivity;
import com.ns.yc.lifehelper.ui.other.myNews.videoNews.VideoNewsActivity;
import com.ns.yc.lifehelper.ui.other.myNews.wyNews.WyNewsActivity;
import com.ns.yc.lifehelper.utils.DialogUtils;
import com.ns.yc.lifehelper.utils.image.ImageUtils;
import com.ns.yc.lifehelper.weight.MarqueeView;
import com.ns.yc.lifehelper.weight.pileCard.ItemEntity;
import com.ns.yc.yccardviewlib.CardViewLayout;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;
import com.yc.cn.ycbannerlib.first.BannerView;
import com.yc.cn.ycbannerlib.first.util.SizeUtil;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import java.util.List;
import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/3/21
 * 描    述：Home主页面
 * 修订历史：
 * <p>
 * v1.5 修改于11月3日，改写代码为MVP架构
 * ================================================
 */
public class HomeFragment extends BaseFragment implements HomeFragmentContract.View {


    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private MainActivity activity;
    private BannerView banner;
    private MarqueeView marqueeView;
    private HomeFragmentContract.Presenter presenter = new HomeFragmentPresenter(this);
    private HomeBlogAdapter adapter;
    private View headerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (activity != null) {
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
        if (banner != null) {
            banner.pause();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (banner != null) {
            banner.resume();
        }
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.bindView(activity);
        presenter.subscribe();
    }


    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position > 0 && adapter.getAllData().size() > position) {
                    Intent intent = new Intent(activity, WebViewActivity.class);
                    intent.putExtra("url", adapter.getAllData().get(position).getUrl());
                    startActivity(intent);
                } else if (position == 0) {
                    startActivity(VideoNewsActivity.class);
                }
            }
        });
    }


    @Override
    public void initData() {
        presenter.getHomeNewsData();
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        adapter = new HomeBlogAdapter(activity);
        initAddHeader();
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        presenter.getHomeNewsData();
                    } else {
                        recyclerView.setRefreshing(false);
                    }
                } else {
                    recyclerView.setRefreshing(false);
                    ToastUtil.showToast(activity,"网络不可用");
                }
            }
        });
    }


    private void initAddHeader() {
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                headerView = View.inflate(activity, R.layout.head_home_main, null);
                return headerView;
            }

            @Override
            public void onBindView(View header) {
                banner = (BannerView) header.findViewById(R.id.banner);
                TextView tvHomeFirst = (TextView) header.findViewById(R.id.tv_home_first);
                TextView tvHomeSecond = (TextView) header.findViewById(R.id.tv_home_second);
                TextView tvHomeThird = (TextView) header.findViewById(R.id.tv_home_third);
                TextView tvHomeFour = (TextView) header.findViewById(R.id.tv_home_four);
                marqueeView = (MarqueeView) header.findViewById(R.id.marqueeView);
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.tv_home_first:
                                startActivity(VideoNewsActivity.class);
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
                };
                tvHomeFirst.setOnClickListener(listener);
                tvHomeSecond.setOnClickListener(listener);
                tvHomeThird.setOnClickListener(listener);
                tvHomeFour.setOnClickListener(listener);
                marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, TextView textView) {
                        //存放自己的博客
                    }
                });
                initBanner();
                initMarqueeView();
            }
        });
    }


    /**
     * 初始化轮播图
     */
    private void initBanner() {
        if (headerView != null && banner != null) {
            List<Object> lists = presenter.getBannerData();
            banner.setHintGravity(1);
            banner.setAnimationDuration(1000);
            banner.setPlayDelay(2000);
            banner.setHintPadding(0, 0, 0, SizeUtil.dip2px(activity, 10));
            banner.setAdapter(new BaseBannerPagerAdapter(activity, lists));
        }
    }


    private void initMarqueeView() {
        if (marqueeView == null) {
            return;
        }
        List<CharSequence> list = presenter.getMarqueeTitle();
        marqueeView.startWithList(list);
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                switch (position) {
                    case 0:
                        DialogUtils.showCustomPopupWindow(activity);
                        break;
                    case 1:
                        Intent intent1 = new Intent(activity, WebViewActivity.class);
                        intent1.putExtra("url", "https://github.com/yangchong211");
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(activity, WebViewActivity.class);
                        intent2.putExtra("url", "http://www.ximalaya.com/zhubo/71989305/");
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void setNewsData(List<HomeBlogEntity> list) {
        if (list != null && list.size() > 0) {
            adapter.clear();
            adapter.addAll(list);
            adapter.notifyDataSetChanged();
            recyclerView.scrollTo(0,0);
            recyclerView.setRefreshing(false);
        }
    }


}

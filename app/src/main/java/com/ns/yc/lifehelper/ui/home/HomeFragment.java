package com.ns.yc.lifehelper.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.api.ConstantImageApi;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.home.adapter.HomeBlogAdapter;
import com.ns.yc.lifehelper.ui.home.bean.HomeBlogEntity;
import com.ns.yc.lifehelper.ui.main.MainActivity;
import com.ns.yc.lifehelper.ui.main.WebViewActivity;
import com.ns.yc.lifehelper.ui.other.myKnowledge.MyKnowledgeActivity;
import com.ns.yc.lifehelper.ui.other.myNews.txNews.TxNewsActivity;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.WxNewsActivity;
import com.ns.yc.lifehelper.ui.other.myNews.wyNews.WyNewsActivity;
import com.ns.yc.lifehelper.ui.weight.MarqueeView;
import com.ns.yc.lifehelper.ui.weight.itemLine.RecycleViewItemLine;
import com.ns.yc.lifehelper.ui.weight.pileCard.ItemEntity;
import com.ns.yc.lifehelper.ui.weight.pileCard.PileLayout;
import com.ns.yc.lifehelper.utils.DialogUtils;
import com.ns.yc.lifehelper.utils.ImageUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/21
 * 描    述：Home主页面
 * 修订历史：
 * ================================================
 */
public class HomeFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate, View.OnClickListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.bga_refresh)
    BGARefreshLayout bgaRefresh;
    private MainActivity activity;
    private HomeBlogAdapter adapter;
    private Banner banner;
    private TextView tv_home_first;
    private TextView tv_home_second;
    private TextView tv_home_third;
    private TextView tv_home_four;
    private View headerView;

    private List<HomeBlogEntity> blog = new ArrayList<>();
    private MarqueeView marqueeView;
    private ArrayList<ItemEntity> dataList;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        if(banner!=null){
            banner.startAutoPlay();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //结束轮播
        if(banner!=null){
            banner.stopAutoPlay();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Constant.isLogin){

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
    public void initListener() {
        tv_home_first.setOnClickListener(this);
        tv_home_second.setOnClickListener(this);
        tv_home_third.setOnClickListener(this);
        tv_home_four.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getWxNews();
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
            getWxData();
        } else {
            ToastUtils.showShortSafe("没有网络");
            refreshLayout.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(NetworkUtils.isConnected()){
            getWxData();
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
        banner = (Banner) header.findViewById(R.id.banner);
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
        PileLayout pileLayout = (PileLayout) header.findViewById(R.id.pileLayout);
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
        }
    }

    /**初始化轮播图*/
    private void initBanner() {
        if(headerView!=null && banner!=null){
            List<String> lists = new ArrayList<>();
            for(int a = 0; a< ConstantImageApi.SPALSH_URLS.length ; a++){
                lists.add(ConstantImageApi.SPALSH_URLS[a]);
            }
            //设置banner样式
            banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
            //设置图片加载器
            banner.setImageLoader(new BannerImageLoader());
            //设置图片集合
            banner.setImages(lists);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.Default);
            //设置标题集合（当banner样式有显示title时）
            //banner.setBannerTitles(titleLists);
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(2000);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.RIGHT);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
    }

    private class BannerImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            ImageUtils.loadImgByPicasso(context, (String) path,imageView);
        }
    }

    private String[] title = {"1.坚持读书，写作，源于内心的动力","2.展翅翱翔，青春我们一路前行","3.欢迎订阅喜马拉雅听书"};
    private void initMarqueeView() {
        if(marqueeView==null){
            return;
        }
        List<CharSequence> list = new ArrayList<>();
        SpannableString ss1 = new SpannableString(title[0]);
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK),  2, title[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.add(ss1);
        SpannableString ss2 = new SpannableString(title[1]);
        ss2.setSpan(new ForegroundColorSpan(Color.BLACK),  2, title[1].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.add(ss2);
        SpannableString ss3 = new SpannableString(title[2]);
        ss3.setSpan(new URLSpan("http://www.ximalaya.com/zhubo/71989305/"), 2, title[2].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.add(ss3);
        marqueeView.startWithList(list);
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                switch (position){
                    case 0:
                        DialogUtils.showCustomPopupWindow(activity);
                        break;
                    case 1:
                        DialogUtils.showCustomAlertDialog(activity);
                        break;
                    case 2:

                        break;
                }
            }
        });
    }


    private void initPileCard(PileLayout pileLayout) {
        initDataList();
        initPile(pileLayout);
    }

    private void initDataList() {
        dataList = new ArrayList<>();
        try {
            InputStream in = activity.getAssets().open("preset.config");
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            String jsonStr = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.optJSONArray("result");
            if (null != jsonArray) {
                int len = jsonArray.length();
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < len; i++) {
                        JSONObject itemJsonObject = jsonArray.getJSONObject(i);
                        ItemEntity itemEntity = new ItemEntity(itemJsonObject);
                        dataList.add(itemEntity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPile(PileLayout pileLayout) {
        pileLayout.setAdapter(new PileLayout.Adapter() {

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
                ImageUtils.loadImgByPicassoWithRound(activity,dataList.get(position).getCoverImageUrl()
                        ,SizeUtils.dp2px(15),R.drawable.bg_stack_blur_default,viewHolder.imageView);
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


    private void getWxNews() {
        blog.clear();
        try {
            InputStream in = activity.getAssets().open("ycBlog.config");
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            String jsonStr = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.optJSONArray("result");
            if (null != jsonArray) {
                int len = jsonArray.length();
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < len; i++) {
                        JSONObject itemJsonObject = jsonArray.getJSONObject(i);
                        HomeBlogEntity blogEntity = new HomeBlogEntity(itemJsonObject);
                        blog.add(blogEntity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getWxData();
    }


    private void getWxData() {
        if (blog != null && blog.size() > 0) {
            adapter.clear();
            adapter.addNewData(blog);
            bgaRefresh.endRefreshing();
            bgaRefresh.scrollTo(0, 0);
        }
    }

}

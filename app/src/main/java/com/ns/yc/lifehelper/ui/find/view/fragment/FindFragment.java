package com.ns.yc.lifehelper.ui.find.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.adapter.BaseBannerPagerAdapter;
import com.ns.yc.lifehelper.base.adapter.BaseDelegateAdapter;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.data.view.activity.DoodleViewActivity;
import com.ns.yc.lifehelper.ui.find.contract.FindFragmentContract;
import com.ns.yc.lifehelper.ui.find.presenter.FindFragmentPresenter;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.view.BookReaderActivity;
import com.ns.yc.lifehelper.ui.other.douban.douBook.view.DouBookActivity;
import com.ns.yc.lifehelper.ui.other.douban.douMovie.view.DouMovieActivity;
import com.ns.yc.lifehelper.ui.other.douban.douMusic.view.DouMusicActivity;
import com.ns.yc.lifehelper.ui.other.gank.view.activity.GanKHomeActivity;
import com.ns.yc.lifehelper.ui.other.gold.view.activity.GoldMainActivity;
import com.ns.yc.lifehelper.ui.other.myNews.videoNews.VideoNewsActivity;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.view.activity.TxWeChatNewsActivity;
import com.ns.yc.lifehelper.ui.other.myPicture.MyPictureActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.SongCiActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.YuanQuActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.view.TangShiFirstActivity;
import com.ns.yc.lifehelper.ui.other.myVideo.MyVideoActivity;
import com.ns.yc.lifehelper.ui.other.notePad.NotePadActivity;
import com.ns.yc.lifehelper.ui.other.workDo.ui.WorkDoActivity;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.ZhiHuNewsActivity;
import com.ns.yc.lifehelper.ui.webView.view.WebViewActivity;
import com.yc.cn.ycbannerlib.BannerView;
import com.yc.cn.ycbannerlib.banner.util.SizeUtil;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : 数据页面，使用阿里巴巴Vlayout框架
 *     revise: v1.4 17年6月8日
 *             v1.5 17年10月3日修改
 * </pre>
 */
public class FindFragment extends BaseFragment<FindFragmentPresenter>
        implements FindFragmentContract.View {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    private MainActivity activity;
    private FindFragmentContract.Presenter presenter = new FindFragmentPresenter(this);
    private List<DelegateAdapter.Adapter> mAdapters;
    private BannerView mBanner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        presenter.bindActivity(activity);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if(activity!=null){
            activity = null;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if(mBanner!=null){
            mBanner.pause();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(mBanner!=null){
            mBanner.resume();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public int getContentView() {
        return R.layout.base_recycler_view;
    }

    @Override
    public void initView() {
        mAdapters = new LinkedList<>();
        initRecyclerView();
    }


    @Override
    public void initListener() {

    }


    @Override
    public void initData() {

    }


    private void initRecyclerView() {
        DelegateAdapter delegateAdapter = presenter.initRecyclerView(recyclerView);
        //把轮播器添加到集合
        BaseDelegateAdapter bannerAdapter = presenter.initBannerAdapter();
        mAdapters.add(bannerAdapter);

        //初始化九宫格
        BaseDelegateAdapter menuAdapter = presenter.initGvMenu();
        mAdapters.add(menuAdapter);

        //初始化
        BaseDelegateAdapter marqueeAdapter = presenter.initMarqueeView();
        mAdapters.add(marqueeAdapter);

        //初始化标题
        BaseDelegateAdapter titleAdapter = presenter.initTitle("豆瓣分享");
        mAdapters.add(titleAdapter);
        //初始化list3
        BaseDelegateAdapter girdAdapter3 = presenter.initList3();
        mAdapters.add(girdAdapter3);

        //初始化标题
        titleAdapter = presenter.initTitle("猜你喜欢");
        mAdapters.add(titleAdapter);
        //初始化list1
        BaseDelegateAdapter girdAdapter = presenter.initList1();
        mAdapters.add(girdAdapter);


        //初始化标题
        titleAdapter = presenter.initTitle("热门新闻");
        mAdapters.add(titleAdapter);
        //初始化list2
        BaseDelegateAdapter linearAdapter = presenter.initList2();
        mAdapters.add(linearAdapter);


        //初始化标题
        titleAdapter = presenter.initTitle("为您精选");
        mAdapters.add(titleAdapter);
        //初始化list3
        BaseDelegateAdapter plusAdapter = presenter.initList4();
        mAdapters.add(plusAdapter);


        //初始化list控件
        titleAdapter = presenter.initTitle("优质新闻");
        mAdapters.add(titleAdapter);
        linearAdapter = presenter.initList5();
        mAdapters.add(linearAdapter);

        //设置适配器
        delegateAdapter.setAdapters(mAdapters);
        recyclerView.requestLayout();
    }


    @Override
    public void setBanner(BannerView mBanner, List<Object> arrayList) {
        this.mBanner = mBanner;
        mBanner.setHintGravity(2);
        mBanner.setAnimationDuration(1000);
        mBanner.setPlayDelay(2000);
        mBanner.setHintPadding(0,0,0, SizeUtil.dip2px(activity,10));
        mBanner.setAdapter(new BaseBannerPagerAdapter(activity, arrayList));
    }


    @Override
    public void setOnclick(int position) {
        switch (position){
            case 0:
                startActivity(GanKHomeActivity.class);
                break;
            case 1:

                break;
            case 2:
                startActivity(NotePadActivity.class);
                break;
            case 3:
                startActivity(ZhiHuNewsActivity.class);
                break;
            case 4:

                break;
            case 5:
                startActivity(DoodleViewActivity.class);
                break;
            case 6:
                startActivity(ZhiHuNewsActivity.class);
                break;
            case 7:
                startActivity(TxWeChatNewsActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    public void setMarqueeClick(int position) {
        switch (position) {
            case 0:
                Intent intent1 = new Intent(activity, WebViewActivity.class);
                intent1.putExtra("url", "https://github.com/yangchong211");
                activity.startActivity(intent1);
                break;
            case 1:
                Intent intent2 = new Intent(activity, WebViewActivity.class);
                intent2.putExtra("url", "http://www.ximalaya.com/zhubo/71989305/");
                activity.startActivity(intent2);
                break;
            default:
                break;
        }
    }


    @Override
    public void setGridClick(int position) {
        switch (position){
            case 0:
                startActivity(BookReaderActivity.class);
                break;
            case 1:

                break;
            case 2:
                startActivity(MyVideoActivity.class);
                break;
            case 3:
                startActivity(MyPictureActivity.class);
                break;
            case 4:
                startActivity(TangShiFirstActivity.class);
                break;
            case 5:
                startActivity(SongCiActivity.class);
                break;
            case 6:
                startActivity(YuanQuActivity.class);
                break;
            case 7:
                startActivity(WorkDoActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    public void setGridClickThird(int position) {
        switch (position){
            case 0:
                startActivity(DouMovieActivity.class);
                break;
            case 1:
                startActivity(DouMusicActivity.class);
                break;
            case 2:
                startActivity(DouBookActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    public void setGridClickFour(int position) {
        switch (position){
            case 0:

                break;
            case 1:
                startActivity(GoldMainActivity.class);
                break;
            case 2:
                startActivity(ZhiHuNewsActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    public void setNewsList2Click(int position, String url) {
        if(position>-1 && url!=null && url.length()>0){
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
        }else if(position==0){
            startActivity(VideoNewsActivity.class);
        }
    }



    @Override
    public void setNewsList5Click(int position, String url) {
        if(position>-1 && url!=null && url.length()>0){
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
        }else if(position==0){
            startActivity(VideoNewsActivity.class);
        }
    }



}

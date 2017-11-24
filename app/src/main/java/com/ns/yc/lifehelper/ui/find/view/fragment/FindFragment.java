package com.ns.yc.lifehelper.ui.find.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BannerImageLoader;
import com.ns.yc.lifehelper.base.BaseApplication;
import com.ns.yc.lifehelper.base.BaseDelegateAdapter;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.base.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.data.view.activity.DoodleViewActivity;
import com.ns.yc.lifehelper.ui.find.contract.FindFragmentContract;
import com.ns.yc.lifehelper.ui.find.presenter.FindFragmentPresenter;
import com.ns.yc.lifehelper.ui.find.view.activity.FastLookActivity;
import com.ns.yc.lifehelper.ui.main.view.activity.MainActivity;
import com.ns.yc.lifehelper.ui.main.view.activity.WebViewActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.view.BookReaderActivity;
import com.ns.yc.lifehelper.ui.other.douBook.view.DouBookActivity;
import com.ns.yc.lifehelper.ui.other.douMovie.view.DouMovieActivity;
import com.ns.yc.lifehelper.ui.other.douMusic.view.DouMusicActivity;
import com.ns.yc.lifehelper.ui.other.gank.view.activity.GanKHomeActivity;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.MobilePlayerActivity;
import com.ns.yc.lifehelper.ui.other.myMusic.MyMusicActivity;
import com.ns.yc.lifehelper.ui.other.myNote.NoteActivity;
import com.ns.yc.lifehelper.ui.other.myPicture.MyPictureActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.SongCiActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.YuanQuActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.view.TangShiFirstActivity;
import com.ns.yc.lifehelper.ui.other.myVideo.MyVideoActivity;
import com.ns.yc.lifehelper.ui.other.timeListener.TimeListenerActivity;
import com.ns.yc.lifehelper.ui.other.toDo.view.ToDoTimerActivity;
import com.ns.yc.lifehelper.ui.other.weather.SevenWeatherActivity;
import com.ns.yc.lifehelper.ui.other.workDo.ui.WorkDoActivity;
import com.yc.cn.ycbaseadapterlib.first.BaseViewHolder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import io.realm.Realm;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/21
 * 描    述：数据页面，使用阿里巴巴Vlayout框架
 * 修订历史：
 * ================================================
 */
public class FindFragment extends BaseFragment implements FindFragmentContract.View {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private MainActivity activity;
    private FindFragmentContract.Presenter presenter = new FindFragmentPresenter(this);
    private List<DelegateAdapter.Adapter> mAdapters;        //存放各个模块的适配器
    private Banner mBanner;
    private Realm realm;


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
        if(mBanner!=null){
            mBanner.startAutoPlay();
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //结束轮播
        if(mBanner!=null){
            mBanner.stopAutoPlay();
        }
        presenter.unSubscribe();
    }

    @Override
    public int getContentView() {
        return R.layout.base_recycler_view;
    }

    @Override
    public void initView() {
        mAdapters = new LinkedList<>();
        initRealm();
        presenter.setActivity(activity);
        initRecyclerView();
    }

    private void initRealm() {
        if(realm ==null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
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

        //初始化折叠式指示器控件
        //initSticky();
        //mAdapters.add(stickyAdapter);

        //初始化list控件
        titleAdapter = presenter.initTitle("优质新闻");
        mAdapters.add(titleAdapter);
        linearAdapter = presenter.initList5();
        mAdapters.add(linearAdapter);

        //设置适配器
        delegateAdapter.setAdapters(mAdapters);
    }


    private void initSticky() {
        final ArrayList<String> mTitleList = new ArrayList<>();
        final ArrayList<Fragment> mFragments = new ArrayList<>();
        StickyLayoutHelper layoutHelper = new StickyLayoutHelper();
        layoutHelper.setAspectRatio(4);
        BaseDelegateAdapter stickyAdapter = new BaseDelegateAdapter(activity, layoutHelper, R.layout.view_vlayout_sticky, 1, Constant.viewType.typeSticky) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                //TabLayout tabLayout = holder.getView(R.id.tab_layout);
                //ViewPager vpContent = holder.getView(R.id.vp_content);
                TabLayout tabLayout = (TabLayout) holder.getItemView().findViewById(R.id.tab_layout);
                ViewPager vpContent = (ViewPager) holder.getItemView().findViewById(R.id.vp_content);
                mTitleList.add("综合");
                mTitleList.add("文学");
                mTitleList.add("文化");
                mTitleList.add("生活");
                mTitleList.add("励志");
                mFragments.add(TestFragment.newInstance("综合"));
                mFragments.add(TestFragment.newInstance("文学"));
                mFragments.add(TestFragment.newInstance("文化"));
                mFragments.add(TestFragment.newInstance("生活"));
                mFragments.add(TestFragment.newInstance("励志"));

                /**
                 * 注意使用的是：getChildFragmentManager，
                 * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
                 * 但会内存溢出，在显示时加载数据
                 */
                FragmentManager childFragmentManager = getChildFragmentManager();
                FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
                BasePagerAdapter myAdapter = new BasePagerAdapter(childFragmentManager, mFragments, mTitleList);
                vpContent.setAdapter(myAdapter);
                // 左右预加载页面的个数
                //vpContent.setOffscreenPageLimit(5);
                myAdapter.notifyDataSetChanged();
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                tabLayout.setupWithViewPager(vpContent);
            }
        };
    }


    @Override
    public void setBanner(Banner mBanner,ArrayList<String> arrayList) {
        this.mBanner = mBanner;
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        mBanner.setImages(arrayList);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //        mBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
        mBanner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                ToastUtils.showShort("轮播图被点击了"+position);
            }
        });
    }

    @Override
    public void setOnclick(int position) {
        switch (position){
            case 0:
                startActivity(GanKHomeActivity.class);
                break;
            case 1:
                startActivity(MobilePlayerActivity.class);
                break;
            case 2:
                startActivity(NoteActivity.class);
                break;
            case 3:
                startActivity(ToDoTimerActivity.class);
                break;
            case 4:
                startActivity(SevenWeatherActivity.class);
                break;
            case 5:
                startActivity(DoodleViewActivity.class);
                break;
            case 6:
                startActivity(FastLookActivity.class);
                break;
            case 7:
                startActivity(TimeListenerActivity.class);
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
        }
    }


    @Override
    public void setGridClick(int position) {
        switch (position){
            case 0:
                startActivity(BookReaderActivity.class);
                break;
            case 1:
                startActivity(MyMusicActivity.class);
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
        }
    }

    @Override
    public void setGridClickFour(int position) {
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
        }
    }


}

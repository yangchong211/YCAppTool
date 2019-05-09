package com.ns.yc.lifehelper.ui.find.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.adapter.BaseBannerPagerAdapter;
import com.ns.yc.lifehelper.base.adapter.BaseDelegateAdapter;
import com.ns.yc.lifehelper.ui.find.contract.FindFragmentContract;
import com.ns.yc.lifehelper.ui.find.presenter.FindFragmentPresenter;
import com.ns.yc.lifehelper.ui.guide.view.activity.SelectFollowActivity;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.yc.cn.ycbannerlib.banner.BannerView;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.mvp.BaseFragment;
import com.ycbjie.library.constant.Constant;

import java.util.LinkedList;
import java.util.List;


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
public class FindFragment extends BaseFragment<FindFragmentPresenter> implements
        FindFragmentContract.View {

    private RecyclerView mRecyclerView;
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
    public void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
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
        DelegateAdapter delegateAdapter = presenter.initRecyclerView(mRecyclerView);
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
        mRecyclerView.requestLayout();
    }


    @Override
    public void setBanner(BannerView mBanner, List<Object> arrayList) {
        this.mBanner = mBanner;
        mBanner.setHintGravity(2);
        mBanner.setAnimationDuration(1000);
        mBanner.setPlayDelay(2000);
        mBanner.setHintPadding(0,0,0, SizeUtils.dp2px(10));
        mBanner.setAdapter(new BaseBannerPagerAdapter(activity, arrayList));
    }


    @Override
    public void setOnclick(int position) {
        //通过路由跳转到某模块的某个页面
        switch (position){
            case 0:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_GANK_ACTIVITY);
                break;
            case 1:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_ANDROID_ACTIVITY);
                break;
            case 2:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_NOTE_ACTIVITY);
                break;
            case 3:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_MUSIC_ACTIVITY);
                break;
            case 4:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_ZHIHU_ACTIVITY);
                break;
            case 5:
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constant.URL,Constant.FLUTTER);
                bundle1.putString(Constant.TITLE,"flutter极致体验的WanAndroid客户端");
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle1);
                break;
            case 6:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_GALLERY_ACTIVITY);
                break;
            case 7:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_NEW_WX_ACTIVITY);
                break;
            default:
                break;
        }
    }


    @Override
    public void setMarqueeClick(int position) {
        switch (position) {
            case 0:
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constant.URL,Constant.GITHUB);
                bundle1.putString(Constant.TITLE,"关于更多内容");
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle1);
                break;
            case 1:
                Bundle bundle2 = new Bundle();
                bundle2.putString(Constant.URL,Constant.ZHI_HU);
                bundle2.putString(Constant.TITLE,"关于我的知乎");
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle2);
                break;
            default:
                break;
        }
    }


    @Override
    public void setGridClick(int position) {
        switch (position){
            case 0:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_VIDEO_VIDEO);
                break;
            case 1:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_SNAPHELPER_ACTIVITY);
                break;
            case 2:
                ActivityUtils.startActivity(SelectFollowActivity.class);
                break;
            case 3:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_BOOK_DOODLE_ACTIVITY);
                break;
            case 4:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_PIN_TU_ACTIVITY);
                break;
            case 5:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_AIR_ACTIVITY);
                break;
            case 6:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_MONKEY_ACTIVITY);
                break;
            case 7:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_WORK_ACTIVITY);
                break;
            default:
                break;
        }
    }


    @Override
    public void setGridClickThird(int position) {
        switch (position){
            case 0:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_DOU_MOVIE_ACTIVITY);
                break;
            case 1:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_DOU_MUSIC_ACTIVITY);
                break;
            case 2:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_DOU_BOOK_ACTIVITY);
                break;
            default:
                break;
        }
    }


    @Override
    public void setGridClickFour(int position) {
        switch (position){
            case 0:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_BANNER_LIST_ACTIVITY);
                break;
            case 1:

                break;
            case 2:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_ZHIHU_ACTIVITY);
                break;
            default:
                break;
        }
    }


    @Override
    public void setNewsList2Click(int position, String url) {
        if(position>-1 && url!=null && url.length()>0){
            Bundle bundle = new Bundle();
            bundle.putString(Constant.URL,url);
            ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle);
        }
    }

    @Override
    public void setNewsList5Click(int position, String url) {
        if(position>-1 && url!=null && url.length()>0){
            Bundle bundle = new Bundle();
            bundle.putString(Constant.URL,url);
            ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle);
        }
    }



}

package com.ns.yc.lifehelper.ui.other.myNews.wyNews;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.base.mvp.BaseActivity;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.myNews.wyNews.bean.TodayNewsChannel;
import com.ns.yc.lifehelper.ui.other.myNews.wyNews.cache.CacheTodayChannel;
import com.ns.yc.lifehelper.api.http.news.TodayNewsModel;
import com.ns.yc.lifehelper.ui.other.myNews.wyNews.view.WyNewsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/28
 * 描    述：今日头条
 * 修订历史：
 * ================================================
 */
public class WyNewsActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.vp_content)
    ViewPager vpContent;

    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private Realm realm;
    private List<String> channel;
    private RealmResults<CacheTodayChannel> cacheTodayChannels;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(realm!=null){
            realm.close();
        }*/
    }

    @Override
    public int getContentView() {
        return R.layout.base_tab_view;
    }

    @Override
    public void initView() {
        initToolBar();
        initRealm();
        readCacheChannel();
        initFragmentList();
        initViewPagerAndTab();
    }

    private void initRealm() {
        if(realm == null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }

    private void initToolBar() {
        toolbarTitle.setText("头条新闻");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getTodayDataChanel();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


    private String[] titles = {"头条","新闻","财经","体育","娱乐","军事","教育","科技","NBA","股票","星座","女性","健康","育儿"};
    private void initFragmentList() {
        if(channel!=null && channel.size()>0){
            for(int a=0 ; a<channel.size() ; a++){
                mTitleList.add(channel.get(a));
                mFragments.add(WyNewsFragment.newInstance(channel.get(a)));
            }
        }else {
            for(int a=0 ; a<titles.length ; a++){
                mTitleList.add(titles[a]);
                mFragments.add(WyNewsFragment.newInstance(titles[a]));
            }
        }
    }

    private void initViewPagerAndTab() {
        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BasePagerAdapter myAdapter = new BasePagerAdapter(supportFragmentManager, mFragments, mTitleList);
        vpContent.setAdapter(myAdapter);
        // 左右预加载页面的个数
        vpContent.setOffscreenPageLimit(4);
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(vpContent);
    }


    private void getTodayDataChanel() {
        TodayNewsModel model = TodayNewsModel.getInstance(WyNewsActivity.this);
        model.getTodayNewsChannel(ConstantALiYunApi.Key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TodayNewsChannel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        readCacheChannel();
                    }

                    @Override
                    public void onNext(TodayNewsChannel todayNewsChannel) {
                        if(todayNewsChannel!=null && todayNewsChannel.getResult()!=null && todayNewsChannel.getResult().size()>0){
                            List<String> result = todayNewsChannel.getResult();
                            if(result!=null && result.size()>0){
                                startCacheChannel(result);
                            }
                        }
                    }
                });
    }


    private void readCacheChannel() {
        initRealm();
        if(realm !=null && realm.where(CacheTodayChannel.class).findAll()!=null){
            cacheTodayChannels = realm.where(CacheTodayChannel.class).findAll();
        } else {
            return;
        }
        channel = new ArrayList<>();
        for(int a = 0; a< cacheTodayChannels.size() ; a++){
            channel.add(cacheTodayChannels.get(a).getChannel());
        }
    }


    private void startCacheChannel(List<String> result) {
        initRealm();
        if(realm !=null && realm.where(CacheTodayChannel.class).findAll()!=null){
            cacheTodayChannels = realm.where(CacheTodayChannel.class).findAll();
        } else {
            return;
        }
        realm.beginTransaction();
        cacheTodayChannels.deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        for(int a=0 ; a<result.size() ; a++){
            CacheTodayChannel news = realm.createObject(CacheTodayChannel.class);
            news.setChannel(result.get(a));
        }
        realm.commitTransaction();
    }


}

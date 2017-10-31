package com.ns.yc.lifehelper.ui.other.myNews.wxNews;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.ConstantALiYunApi;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.base.BaseApplication;
import com.ns.yc.lifehelper.base.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.TxWeChatNewsActivity;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.bean.WxNewsTypeBean;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.cache.CacheWxChannel;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.WxNewsModel;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.view.NewsSearchActivity;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.view.WxNewsFragment;

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
 * 创建日期：2017/8/30
 * 描    述：微信新闻
 * 修订历史：
 * ================================================
 */
public class WxNewsActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.stl_tab)
    SlidingTabLayout stlTab;
    @Bind(R.id.vp_content)
    ViewPager vpContent;
    private List<String> title = new ArrayList<>();
    private List<String> chanelId = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private Realm realm;
    private RealmResults<CacheWxChannel> cacheWxChannels;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(realm!=null){
            realm.close();
        }*/
    }

    @Override
    public int getContentView() {
        return R.layout.base_stl_tab_view;
    }

    @Override
    public void initView() {
        initToolBar();
        initRealm();
        initTabData();
    }

    private void initRealm() {
        if(realm == null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }


    private void initToolBar() {
        toolbarTitle.setText("头条新闻");
        llSearch.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public void initListener() {
        llSearch.setOnClickListener(this);
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getWxDataChanel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_search:
                Intent intent = new Intent(WxNewsActivity.this,NewsSearchActivity.class);
                intent.putExtra("type","wx");
                startActivity(intent);
                break;
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_news_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.other:
                startActivity(TxWeChatNewsActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private String[] tabTitles = {"热门","推荐","段子","养生","私房话","八卦"};
    private String[] tabIds = {"1","2","3","4","5","6"};
    private void initTabData() {
        List<String> titles = new ArrayList<>();
        for(int a=0 ; a<tabTitles.length ; a++){
            fragments.add(WxNewsFragment.newInstance(tabIds[a]));
            titles.add(tabTitles[a]);
        }
        BasePagerAdapter adapter = new BasePagerAdapter(getSupportFragmentManager(),fragments,titles);
        vpContent.setAdapter(adapter);
        vpContent.setCurrentItem(0);
        vpContent.setOffscreenPageLimit(6);

        stlTab.setIndicatorGravity(Gravity.BOTTOM);
        stlTab.setViewPager(vpContent);
        stlTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }



    private void getWxDataChanel() {
        WxNewsModel model = WxNewsModel.getInstance(WxNewsActivity.this);
        model.getWxNewsChannel(ConstantALiYunApi.Key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WxNewsTypeBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        readCacheChannel();
                    }

                    @Override
                    public void onNext(WxNewsTypeBean wxNewsTypeBean) {
                        if(wxNewsTypeBean!=null && wxNewsTypeBean.getResult()!=null && wxNewsTypeBean.getResult().size()>0){
                            List<WxNewsTypeBean.ResultBean> result = wxNewsTypeBean.getResult();
                            for (int a=0 ; a<result.size() ; a++){
                                title.add(result.get(a).getChannel());
                                chanelId.add(result.get(a).getChannelid());
                                startCacheChannel(result);
                            }
                        }
                    }
                });
    }

    private void readCacheChannel() {
        initRealm();
        if(realm!=null && realm.where(CacheWxChannel.class).findAll()!=null){
            cacheWxChannels = realm.where(CacheWxChannel.class).findAll();
        } else {
            return;
        }
        title.clear();
        chanelId.clear();
        for(int a=0 ; a<cacheWxChannels.size() ; a++){
            CacheWxChannel cacheWxChannel = new CacheWxChannel();
            cacheWxChannel.setChannel(cacheWxChannel.getChannel());
            cacheWxChannel.setChannelid(cacheWxChannel.getChannelid());

            title.add(cacheWxChannel.getChannel());
            chanelId.add(cacheWxChannel.getChannelid());
        }
    }

    private void startCacheChannel(List<WxNewsTypeBean.ResultBean> result) {
        initRealm();
        if(realm!=null && realm.where(CacheWxChannel.class).findAll()!=null){
            cacheWxChannels = realm.where(CacheWxChannel.class).findAll();
        } else {
            return;
        }
        realm.beginTransaction();
        cacheWxChannels.deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        for(int a=0 ; a<result.size() ; a++){
            CacheWxChannel wxNews = realm.createObject(CacheWxChannel.class);
            wxNews.setChannelid(cacheWxChannels.get(a).getChannelid());
            wxNews.setChannel(cacheWxChannels.get(a).getChannel());
        }
        realm.commitTransaction();
    }


}

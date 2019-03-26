package com.ycbjie.news.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pedaily.yc.ycdialoglib.loading.ViewLoading;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.adapter.BasePagerAdapter;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.news.R;
import com.ycbjie.news.api.TodayNewsModel;
import com.ycbjie.news.model.TodayNewsChannel;
import com.ycbjie.news.ui.fragment.WyNewsFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;




/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/3/28
 *     desc  : 今日头条
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_WY_NEWS_ACTIVITY)
public class WyNewsActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private TabLayout tabLayout;
    private ViewPager vpContent;
    private ArrayList<String> channel = new ArrayList<>();
    /**
     * 请求头：Authorization APPCODE bad05976d99e41d198ff513a4fd519ea
     */
    public static final String Key = "APPCODE bad05976d99e41d198ff513a4fd519ea";

    @Override
    public int getContentView() {
        return R.layout.base_tab_view;
    }

    @Override
    public void initView() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        tabLayout = findViewById(R.id.tab_layout);
        vpContent = findViewById(R.id.vp_content);
        initToolBar();
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
        ViewLoading.show(this);
        getTodayDataChanel();
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.ll_title_menu) {
            finish();
        }
    }

    private void initFragmentList() {
        ArrayList<String> mTitleList = new ArrayList<>();
        ArrayList<Fragment> mFragments = new ArrayList<>();
        String[] titles = {"头条","新闻","财经","体育","娱乐","军事","教育",
                "科技","NBA","股票","星座","女性","健康","育儿"};
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

        /*
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BasePagerAdapter myAdapter = new BasePagerAdapter(supportFragmentManager, mFragments, mTitleList);
        vpContent.setAdapter(myAdapter);
        // 左右预加载页面的个数
        vpContent.setOffscreenPageLimit(mFragments.size());
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(vpContent);
    }



    private void getTodayDataChanel() {
        TodayNewsModel model = TodayNewsModel.getInstance(WyNewsActivity.this);
        model.getTodayNewsChannel(WyNewsActivity.Key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TodayNewsChannel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TodayNewsChannel todayNewsChannel) {
                        if(todayNewsChannel!=null && todayNewsChannel.getResult()!=null && todayNewsChannel.getResult().size()>0){
                            List<String> result = todayNewsChannel.getResult();
                            if(result!=null && result.size()>0){
                                channel.addAll(result);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {
                        ViewLoading.dismiss(WyNewsActivity.this);
                        initFragmentList();
                    }
                });
    }




}

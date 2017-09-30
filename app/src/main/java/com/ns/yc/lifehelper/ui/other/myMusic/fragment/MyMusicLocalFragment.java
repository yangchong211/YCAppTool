package com.ns.yc.lifehelper.ui.other.myMusic.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.other.myMusic.MyMusicActivity;
import com.ns.yc.lifehelper.ui.other.myMusic.activity.MyMusicLocalActivity;
import com.ns.yc.lifehelper.ui.other.myMusic.weight.LocalMenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/25
 * 描    述：我的音乐，本地音乐页面
 * 修订历史：
 * ================================================
 */
public class MyMusicLocalFragment extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.music_local)
    LocalMenuItem musicLocal;
    @Bind(R.id.music_recent)
    LocalMenuItem musicRecent;
    @Bind(R.id.music_download)
    LocalMenuItem musicDownload;
    @Bind(R.id.music_singers)
    LocalMenuItem musicSingers;
    @Bind(R.id.music_mv)
    LocalMenuItem musicMv;
    @Bind(R.id.arrow)
    ImageView arrow;
    @Bind(R.id.list_count)
    TextView listCount;
    @Bind(R.id.iv_more)
    ImageView ivMore;
    @Bind(R.id.play_list)
    RelativeLayout playList;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private MyMusicActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MyMusicActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_my_music_listen;
    }

    @Override
    public void initView() {
        initLocalData();
        initSwipeRefresh();
    }

    @Override
    public void initListener() {
        musicLocal.setOnClickListener(this);
        musicRecent.setOnClickListener(this);
        musicDownload.setOnClickListener(this);
        musicSingers.setOnClickListener(this);
        musicMv.setOnClickListener(this);
        arrow.setOnClickListener(this);
        ivMore.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    /**
     * 初始化数据
     * 这个后期做，应该是本地扫描，数据要保存到数据库中
     */
    private void initLocalData() {
        musicLocal.setCount(5);
        musicRecent.setCount(1);
        musicDownload.setCount(50);
        musicSingers.setCount(10);
        musicMv.setCount(5);
        musicLocal.showSpeaker();
    }


    private void initSwipeRefresh() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_local:
                startActivity(MyMusicLocalActivity.class);
                break;
            case R.id.music_recent:

                break;
            case R.id.music_download:

                break;
            case R.id.music_singers:

                break;
            case R.id.music_mv:

                break;
            case R.id.arrow:
                arrowRotateAnimation();
                break;
            case R.id.iv_more:

                break;
        }
    }

    private float fromDegrees = 0;
    private float toDegrees = 90;
    private void arrowRotateAnimation() {
        Animation rotate = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.startAnimation(rotate);
        float tempDegrees = fromDegrees;
        fromDegrees = toDegrees;
        toDegrees = tempDegrees;
    }

}

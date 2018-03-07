package com.ns.yc.lifehelper.ui.other.mobilePlayer;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.fragment.MobileAudioFragment;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.fragment.MobileVideoFragment;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2015/8/31
 * 描    述：音乐播放器页面
 * 修订历史：
 * ================================================
 */
public class MobilePlayerActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.tv_video)
    TextView tvVideo;
    @Bind(R.id.tv_audio)
    TextView tvAudio;
    @Bind(R.id.view_indicator)
    View viewIndicator;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private int indicatorWidth;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_mobile_play_home;
    }

    @Override
    public void initView() {
        initToolBar();
        initFragment();
        initIndicator();
    }


    private void initToolBar() {
        toolbarTitle.setText("播放器");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        tvAudio.setOnClickListener(this);
        tvVideo.setOnClickListener(this);
        // 初始化的时候ViewPager不会触发这个监听器
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            /** 当某一个页面被选择的时候 */
            @Override
            public void onPageSelected(int position) {
                changeTitleState(position == 0);
            }

            /** 当页面滚动的时候 */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scrollIndicator(position, positionOffset);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.tv_video:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv_audio:
                viewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    private void initIndicator() {
        // 初始化指示线的宽
        indicatorWidth = ScreenUtils.getScreenWidth() / fragments.size();
        viewIndicator.getLayoutParams().width = indicatorWidth;
        // 通知刷新布局参数
        viewIndicator.requestLayout();
    }


    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new MobileVideoFragment());
        fragments.add(new MobileAudioFragment());
        viewPager.setAdapter(new BasePagerAdapter(getSupportFragmentManager(), fragments));
    }

    /**
     * 滚动指示线
     * @param position
     * @param positionOffset
     */
    private void scrollIndicator(int position, float positionOffset) {
        float translationX = indicatorWidth * position + indicatorWidth * positionOffset;
        ViewCompat.setTranslationX(viewIndicator, translationX);    // 移动指示的位置
    }

    /**
     * 改变标题状态：颜色、大小
     * @param selectedVideo 指示是否选择了视频标题
     */
    private void changeTitleState(boolean selectedVideo) {
        tvVideo.setSelected(selectedVideo); // 设置选择状态
        tvAudio.setSelected(!selectedVideo);// 设置选择状态

        scaleView(tvVideo, selectedVideo ? 1.2f : 1.0f);    // 缩放视频标题
        scaleView(tvAudio, !selectedVideo ? 1.2f : 1.0f);   // 缩放音频标题
    }

    /**
     * 缩放指定的view
     * @param view 需要缩放的View
     * @param scale 需要缩放的比例
     */
    private void scaleView(TextView view, float scale) {
        ViewCompat.animate(view).scaleX(scale).scaleY(scale);
    }

}

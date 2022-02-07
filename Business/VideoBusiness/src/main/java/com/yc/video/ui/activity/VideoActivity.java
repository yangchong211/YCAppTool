package com.yc.video.ui.activity;

import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yc.configlayer.arounter.RouterConfig;
import com.yc.fragmentlib.BackHandlerHelper;
import com.yc.library.base.adapter.BasePagerAdapter;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.video.ui.fragment.VideoArticleFragment;
import com.ycbjie.video.R;

import java.util.ArrayList;
import java.util.List;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 视频页面
 *     revise:
 * </pre>
 */
@Route(path = RouterConfig.Video.ACTIVITY_VIDEO_VIDEO)
public class VideoActivity extends BaseActivity {

    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private TabLayout tabLayout;
    private ViewPager vpContent;

    @Override
    public int getContentView() {
        return R.layout.base_tab_view;
    }

    @Override
    public void initView() {
        StateAppBar.setStatusBarColor(this,
                ContextCompat.getColor(this, R.color.black));
        initFindViewById();
        initToolBar();
    }

    private void initFindViewById() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        tabLayout = findViewById(R.id.tab_layout);
        vpContent = findViewById(R.id.vp_content);
    }


    private void initToolBar() {
        toolbarTitle.setText("我的视频");
        llTitleMenu.setOnClickListener(v -> finish());
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        List<Fragment> fragmentList = new ArrayList<>();
        String[] categoryId = this.getResources().getStringArray(R.array.mobile_video_id);
        String[] categoryName = this.getResources().getStringArray(R.array.mobile_video_name);
        ArrayList<String> title = new ArrayList<>();
        for (int i = 0; i < categoryId.length; i++) {
            Fragment fragment = VideoArticleFragment.newInstance(categoryId[i]);
            fragmentList.add(fragment);
            title.add(categoryName[i]);
        }
        FragmentManager manager = getSupportFragmentManager();
        BasePagerAdapter adapter = new BasePagerAdapter(manager, fragmentList, title);
        vpContent.setAdapter(adapter);
        tabLayout.setupWithViewPager(vpContent);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        vpContent.setOffscreenPageLimit(categoryId.length);
    }



    /**
     * 这个是处理返回键的逻辑
     */
    private long lastBackPress;
    public boolean backHandled = true;
    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            if (System.currentTimeMillis() - lastBackPress < 1000) {
                super.onBackPressed();
            } else {
                lastBackPress = System.currentTimeMillis();
                finish();
            }
        }
    }

}

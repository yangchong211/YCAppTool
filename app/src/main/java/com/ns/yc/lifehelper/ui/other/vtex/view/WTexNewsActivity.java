package com.ns.yc.lifehelper.ui.other.vtex.view;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.base.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.vtex.view.activity.WTNoteActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class WTexNewsActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.iv_add)
    FrameLayout ivAdd;
    @Bind(R.id.vp_content)
    ViewPager vpContent;

    public static String[] typeStr = {"技术", "创意", "好玩", "Apple", "酷工作", "交易", "城市", "问与答", "最热", "全部", "R2"};
    public static String[] type = {"tech", "creative", "play", "apple", "jobs", "deals", "city", "qna", "hot", "all", "r2"};
    List<VTexPagerFragment> fragments = new ArrayList<>();
    List<String> title = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.base_add_tab_view;
    }


    @Override
    public void initView() {
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(vpContent);
        for (int i = 0; i < type.length; i++) {
            VTexPagerFragment fragment = VTexPagerFragment.getInstance(type[i]);
            fragments.add(fragment);
            title.add(typeStr[i]);
        }
        BasePagerAdapter mAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragments,title);
        vpContent.setAdapter(mAdapter);
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
    }


    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.iv_add:
                startActivity(WTNoteActivity.class);
                break;
        }
    }


}

package com.yc.common.vp2;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.yc.basevpadapter.viewpager2.BaseFragmentStateAdapter;
import com.yc.basevpadapter.viewpager2.OnPageChangeCallback;
import com.yc.common.R;
import com.yc.library.base.mvp.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewPager2Activity extends BaseActivity {

    private ViewPager2 vpPager;
    private BaseFragmentStateAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    protected void onDestroy() {
        super.onDestroy();
        vpPager.unregisterOnPageChangeCallback(changeCallback);
    }

    @Override
    public int getContentView() {
        return R.layout.base_view_pager2;
    }

    @Override
    public void initView() {
        vpPager = findViewById(R.id.vp_pager);

        fragments = new ArrayList<>();
        // fragments
        fragments.add(ViewPagerFragment.newInstance("聊天"));
        fragments.add(ViewPagerFragment.newInstance("通讯录"));
        fragments.add(ViewPagerFragment.newInstance("发现"));
        fragments.add(ViewPagerFragment.newInstance("我"));
        fragments.add(ViewPagerFragment.newInstance("聊天"));
        fragments.add(ViewPagerFragment.newInstance("通讯录"));
        fragments.add(ViewPagerFragment.newInstance("发现"));
        fragments.add(ViewPagerFragment.newInstance("我"));

        adapter = new BaseFragmentStateAdapter(this, fragments);
        vpPager.registerOnPageChangeCallback(changeCallback);
        vpPager.setCurrentItem(0);
        adapter.update(fragments);
        vpPager.setAdapter(adapter);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    private final OnPageChangeCallback changeCallback = new OnPageChangeCallback(){
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
        }
    };

}

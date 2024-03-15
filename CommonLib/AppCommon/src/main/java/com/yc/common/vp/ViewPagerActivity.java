package com.yc.common.vp;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.yc.baseclasslib.adapter.BasePagerStateAdapter;
import com.yc.common.R;
import com.yc.common.vp2.TextFragment;
import com.yc.library.base.mvp.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends BaseActivity {

    private ViewPager vpPager;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.base_view_pager;
    }

    @Override
    public void initView() {
        vpPager = findViewById(R.id.vp_pager);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

        List<Fragment> fragments = new ArrayList<>();
        // fragments
        fragments.add(TextFragment.newInstance("聊天"));
        fragments.add(TextFragment.newInstance("通讯录"));
        fragments.add(TextFragment.newInstance("发现"));
        fragments.add(TextFragment.newInstance("我"));
        fragments.add(TextFragment.newInstance("聊天"));
        fragments.add(TextFragment.newInstance("通讯录"));
        fragments.add(TextFragment.newInstance("发现"));
        fragments.add(TextFragment.newInstance("我"));
        fragments.add(TextFragment.newInstance("聊天"));
        fragments.add(TextFragment.newInstance("通讯录"));
        fragments.add(TextFragment.newInstance("发现"));
        fragments.add(TextFragment.newInstance("我"));

        BasePagerStateAdapter adapter = new BasePagerStateAdapter(this.getSupportFragmentManager(), fragments);
        vpPager.setAdapter(adapter);
        vpPager.setCurrentItem(0);
        adapter.notifyDataSetChanged();
    }
}

package com.yc.common.vp2;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.yc.baseclasslib.viewpager2.BaseFragmentStateAdapter;
import com.yc.baseclasslib.viewpager2.DiffFragmentStateAdapter;
import com.yc.baseclasslib.viewpager2.OnPageChangeCallback;
import com.yc.common.R;
import com.yc.library.base.mvp.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewPager2Activity extends BaseActivity {

    private ViewPager2 vpPager;

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

        BaseFragmentStateAdapter adapter = new BaseFragmentStateAdapter(this, fragments);
        vpPager.registerOnPageChangeCallback(changeCallback);
        //vpPager.setCurrentItem(0);
        adapter.update(fragments);
        vpPager.setAdapter(adapter);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    private final ViewPager2.OnPageChangeCallback changeCallback = new OnPageChangeCallback(){
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
        }
    };

}

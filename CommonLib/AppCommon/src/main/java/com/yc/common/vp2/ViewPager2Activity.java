package com.yc.common.vp2;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.yc.baseclasslib.viewpager2.BaseFragmentStateAdapter;
import com.yc.baseclasslib.viewpager2.OnPageChangeCallback;
import com.yc.common.R;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.toastutils.ToastUtils;

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
        fragments.add(ViewPagerFragment.newInstance("哈哈哈哈，要下一页啦"));

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

        private boolean isHomeLastPage = false;
        private boolean isHomeFirstPage = false;
        private boolean isHomeDragPage = false;

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            //获取最后一页/position等于最后一个元素
            isHomeLastPage = position == fragments.size() - 1;
            isHomeFirstPage = position == 0;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            //1.判断是否是最后一个元素
            //2.当前是否滑动状态,
            //3.positionOffsetPixels可偏移量为0
            if (isHomeLastPage && isHomeDragPage && positionOffsetPixels == 0) {
                //当前页是最后一页，并且是拖动状态，并且像素偏移量为0
                ToastUtils.showRoundRectToast("当前页是最后一页");
            }
            if (isHomeFirstPage && isHomeDragPage && positionOffsetPixels == 0) {
                //当前页是最后一页，并且是拖动状态，并且像素偏移量为0
                ToastUtils.showRoundRectToast("当前页没有上一页");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
            // 0：什么都没做 1：开始滑动 2：滑动结束 滚动监听
            isHomeDragPage = state == ViewPager2.SCROLL_STATE_DRAGGING;
            //判断最后一个就不加载了
            if (state == ViewPager2.SCROLL_STATE_IDLE){
                loadMore();
            }
        }
    };

    private void loadMore() {
        fragments.clear();
        // fragments
        fragments.add(ViewPagerFragment.newInstance("聊天"));
        fragments.add(ViewPagerFragment.newInstance("通讯录"));
        fragments.add(ViewPagerFragment.newInstance("发现"));
        fragments.add(ViewPagerFragment.newInstance("我"));
        fragments.add(ViewPagerFragment.newInstance("聊天"));
        fragments.add(ViewPagerFragment.newInstance("通讯录"));
        fragments.add(ViewPagerFragment.newInstance("发现"));
        fragments.add(ViewPagerFragment.newInstance("我"));
        // fragments
        fragments.add(ViewPagerFragment.newInstance("聊天"));
        fragments.add(ViewPagerFragment.newInstance("通讯录"));
        fragments.add(ViewPagerFragment.newInstance("发现"));
        fragments.add(ViewPagerFragment.newInstance("我"));
        fragments.add(ViewPagerFragment.newInstance("聊天"));
        fragments.add(ViewPagerFragment.newInstance("通讯录"));
        fragments.add(ViewPagerFragment.newInstance("发现"));
        fragments.add(ViewPagerFragment.newInstance("我"));
        fragments.add(ViewPagerFragment.newInstance("逗比"));
        fragments.add(ViewPagerFragment.newInstance("有下一页数据呢"));
        adapter.update(fragments);
    }

}

package com.yc.baseclasslib.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : FragmentPagerAdapter 简单封装
 *     revise: ViewPager的适配器
 * </pre>
 */
public final class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    /**
     * FragmentPageAdapter在每次切换页面时，只是将Fragment进行分离。
     * 适合页面较少的Fragment使用以保存一些内存，对系统内存不会多大影响。
     */

    /**
     * Fragment 集合
     */
    private final List<Fragment> mFragmentSet = new ArrayList<>();
    /**
     * Fragment 标题
     */
    private final List<CharSequence> mFragmentTitle = new ArrayList<>();

    /**
     * 当前显示的Fragment
     */
    private Fragment mShowFragment;

    /**
     * 当前 ViewPager
     */
    private ViewPager mViewPager;

    /**
     * 设置成懒加载模式
     */
    private boolean mLazyMode = true;

    public BaseFragmentPagerAdapter(FragmentActivity activity) {
        this(activity.getSupportFragmentManager());
    }

    public BaseFragmentPagerAdapter(Fragment fragment) {
        this(fragment.getChildFragmentManager());
    }

    public BaseFragmentPagerAdapter(FragmentManager manager) {
        super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getCount() {
        return mFragmentSet.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mFragmentTitle.size()>0) {
            return mFragmentTitle.get(position);
        } else {
            return "";
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        if (getShowFragment() != object) {
            // 记录当前的Fragment对象
            mShowFragment = (Fragment) object;
        }
    }

    /**
     * 添加 Fragment
     */
    public void addFragment(Fragment fragment) {
        addFragment(fragment, null);
    }

    public void addFragment(Fragment fragment, CharSequence title) {
        mFragmentSet.add(fragment);
        mFragmentTitle.add(title);

        if (mViewPager == null) {
            return;
        }

        notifyDataSetChanged();
        if (mLazyMode) {
            mViewPager.setOffscreenPageLimit(getCount());
        } else {
            mViewPager.setOffscreenPageLimit(1);
        }
    }

    public void addFragmentList(List<Fragment> fragmentList) {
        this.mFragmentSet.clear();
        mFragmentSet.addAll(fragmentList);
        notifyDataSetChanged();
    }

    public void addFragmentList(List<Fragment> fragmentList , List<String> titleList) {
        this.mFragmentSet.clear();
        mFragmentSet.addAll(fragmentList);
        this.mFragmentTitle.clear();
        mFragmentTitle.addAll(titleList);
        notifyDataSetChanged();
    }

    /**
     * 获取当前的Fragment
     */
    public Fragment getShowFragment() {
        return mShowFragment;
    }

    /**
     * 获取某个 Fragment 的索引（没有就返回 -1）
     */
    public int getFragmentIndex(Class<? extends Fragment> clazz) {
        if (clazz == null) {
            return -1;
        }
        for (int i = 0; i < mFragmentSet.size(); i++) {
            if (clazz.getName().equals(mFragmentSet.get(i).getClass().getName())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
        super.startUpdate(container);
        if (container instanceof ViewPager) {
            // 记录绑定 ViewPager
            mViewPager = (ViewPager) container;
            refreshLazyMode();
        }
    }

    /**
     * 设置懒加载模式
     */
    public void setLazyMode(boolean lazy) {
        mLazyMode = lazy;
        refreshLazyMode();
    }

    /**
     * 刷新加载模式
     */
    private void refreshLazyMode() {
        if (mViewPager == null) {
            return;
        }

        if (mLazyMode) {
            // 设置成懒加载模式（也就是不限制 Fragment 展示的数量）
            mViewPager.setOffscreenPageLimit(getCount());
        } else {
            mViewPager.setOffscreenPageLimit(1);
        }
    }
}

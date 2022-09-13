package com.yc.baseclasslib.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * <pre>
 *     @author 杨充
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2017/01/30
 *     desc  : ViewPager的适配器
 *     revise:
 * </pre>
 */
public class BasePagerStateAdapter extends FragmentStatePagerAdapter {


    /**
     * FragmentPageStateAdapter在每次切换页面的时候，是将Fragment进行回收。
     * 适合页面较多的Fragment使用，这样就不会消耗更多的内存
     */

    private final List<?> mFragment;

    /**
     * 接收首页传递的标题
     */
    public BasePagerStateAdapter(FragmentManager fm, List<?> mFragment) {
        super(fm);
        this.mFragment = mFragment;
    }

    /**
     * 返回与指定位置相关联的fragment
     * @param position          指定位置的索引
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return (Fragment) mFragment.get(position);
    }

    /**
     * 返回fragment的数量
     * @return  数量
     */
    @Override
    public int getCount() {
        return mFragment == null ? 0 : mFragment.size();
    }

}

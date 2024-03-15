package com.yc.baseclasslib.viewpager2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author 杨充
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2019/01/30
 *     desc  : ViewPager2的适配器
 *     revise:
 * </pre>
 */
public class BaseFragmentStateAdapter extends FragmentStateAdapter {

    /**
     * Fragment 集合
     */
    public final List<Fragment> mFragments = new ArrayList<>();

    /**
     * 如果宿主是Activity，则使用这个
     *
     * @param fragmentActivity activity
     * @param fragments        fragment集合
     */
    public BaseFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
        super(fragmentActivity);
        if (fragments != null) {
            mFragments.addAll(fragments);
        }
    }

    /**
     * 如果宿主是Fragment，则使用这个
     *
     * @param fragment  fragment
     * @param fragments fragment集合
     */
    public BaseFragmentStateAdapter(@NonNull Fragment fragment, List<Fragment> fragments) {
        super(fragment);
        if (fragments != null) {
            mFragments.addAll(fragments);
        }
    }

    /**
     * 如果宿主是Fragment，则使用这个。和第二个构造没什么区别
     *
     * @param fragmentManager fragmentManager
     * @param lifecycle       lifecycle
     * @param fragments       fragment集合
     */
    public BaseFragmentStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> fragments) {
        super(fragmentManager, lifecycle);
        if (fragments != null) {
            mFragments.addAll(fragments);
        }
    }

    /**
     * 更新数据
     *
     * @param fragments fragment数据
     */
    public void update(List<Fragment> fragments) {
        mFragments.clear();
        mFragments.addAll(fragments);
        notifyDataSetChanged();
    }

    /**
     * 用来根据position创建fragment
     *
     * @param position 索引
     * @return fragment
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    /**
     * 返回Item的数量
     *
     * @return 数量
     */
    @Override
    public int getItemCount() {
        return mFragments.size();
    }

}

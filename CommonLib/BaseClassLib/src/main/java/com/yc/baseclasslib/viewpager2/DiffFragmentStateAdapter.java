package com.yc.baseclasslib.viewpager2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/**
 * <pre>
 *     @author 杨充
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2019/01/30
 *     desc  : ViewPager2的适配器
 *     revise: 注意使用这个会闪一下
 * </pre>
 */
public class DiffFragmentStateAdapter extends BaseFragmentStateAdapter {

    public DiffFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
        super(fragmentActivity, fragments);
    }

    public DiffFragmentStateAdapter(@NonNull Fragment fragment, List<Fragment> fragments) {
        super(fragment, fragments);
    }

    public DiffFragmentStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> fragments) {
        super(fragmentManager, lifecycle, fragments);
    }

    /**
     * 并不是说getItemId()只会在DiffUtil中生效，getItemId()和ViewPager中的效果类似。
     * 只要同一个position下getItemId()的return的Long不同，就会触发重新createFragment()。
     * 虽然可以完成更新Fragment的效果，但是会带来体验的瑕疵：会“闪一下”。
     *
     * @param position 索引
     * @return 返回id(取对象名称的hashcode值作为id)
     */
    @Override
    public long getItemId(int position) {
        Fragment fragment = mFragments.get(position);
        if (fragment != null) {
            return fragment.getClass().getSimpleName().hashCode();
        }
        return super.getItemId(position);
    }

    /**
     * 判断是否包含
     *
     * @param itemId item属性id
     * @return 是否包含
     */
    @Override
    public boolean containsItem(long itemId) {
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            if (fragment != null) {
                return fragment.getClass().getSimpleName().hashCode() == itemId;
            }
        }
        return super.containsItem(itemId);
    }

    public void diffUpdate(List<Fragment> newFragments){
        //助DiffUtil更新数据
        DiffUtil.Callback callback = new ViewPagerDiffCallback<>(mFragments, newFragments);
        //经过比对得到差异结果
        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(callback);
        //注意这里要重新设置Adapter中的数据
        mFragments.clear();
        mFragments.addAll(newFragments);
        //将数据传给adapter，最终通过adapter.notifyItemXXX更新数据
        diff.dispatchUpdatesTo(this);
    }

    @Override
    public void update(List<Fragment> newFragments) {
        super.update(newFragments);
    }

}

package com.yc.baseclasslib.viewpager2;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author 杨充
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2019/01/30
 *     desc  : ViewPager2的diff操作
 *     revise: DiffUtil 实用程序类依靠 ID 来标识项。如果使用 ViewPager2 分页浏览可变集合，则还必须替换 getItemId() 和 containsItem()。
 * </pre>
 */
public class ViewPagerDiffCallback<T extends Fragment> extends DiffUtil.Callback {

    private final List<T> oldList = new ArrayList<>();
    private final List<T> newList = new ArrayList<>();

    public ViewPagerDiffCallback(List<T> oldList, List<T> newList) {
        this.oldList.clear();
        this.newList.clear();

        this.oldList.addAll(oldList);
        this.newList.addAll(newList);
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    /**
     * 判断item是否相同
     * DiffUtil调用来决定两个对象是否代表相同的Item。true表示两个Item相同(表示View可以复用)，false表示不相同(View不可以复用)
     * 例如，如果你的项目有唯一的id，这个方法应该检查它们的id是否相等。
     *
     * @param oldItemPosition 老数据
     * @param newItemPosition 新数据
     * @return
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldList.size() > oldItemPosition && newList.size() > newItemPosition) {
            return oldList.get(oldItemPosition).getClass().getSimpleName().hashCode() ==
                    newList.get(newItemPosition).getClass().getSimpleName().hashCode();
        }
        return false;
    }

    /**
     * 比较两个Item是否有相同的内容(用于判断Item的内容是否发生了改变)，
     * 该方法只有当areItemsTheSame (int, int)返回true时才会被调用。
     *
     * @param oldItemPosition 老数据
     * @param newItemPosition 新数据
     * @return
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldList.size() > oldItemPosition && newList.size() > newItemPosition) {
            return oldList.get(oldItemPosition) == newList.get(newItemPosition);
        }
        return false;
    }

    /**
     * 该方法执行时机：areItemsTheSame(int, int)返回true 并且 areContentsTheSame(int, int)返回false
     * 该方法返回Item中的变化数据，用于只更新Item中变化数据对应的UI
     *
     * @param oldItemPosition 老数据
     * @param newItemPosition 新数据
     * @return
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}

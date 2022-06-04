package com.yc.adapter;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/9/18
 *     desc  : child点击事件
 *     revise: https://github.com/yangchong211/YCGroupAdapter
 * </pre>
 */
public interface OnChildClickListener {
    /**
     * child点击事件
     * @param adapter                           adapter适配器
     * @param holder                            holder
     * @param groupPosition                     group索引
     * @param childPosition                     group中的child索引
     */
    void onChildClick(AbsGroupAdapter adapter, GroupViewHolder holder,
                      int groupPosition, int childPosition);
}

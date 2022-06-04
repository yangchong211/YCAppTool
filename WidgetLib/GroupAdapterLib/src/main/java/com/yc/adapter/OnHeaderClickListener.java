package com.yc.adapter;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/9/18
 *     desc  : header点击事件
 *     revise: https://github.com/yangchong211/YCGroupAdapter
 * </pre>
 */
public interface OnHeaderClickListener {
    /**
     * header点击事件
     * @param adapter                           adapter适配器
     * @param holder                            holder
     * @param groupPosition                     group索引
     */
    void onHeaderClick(AbsGroupAdapter adapter, GroupViewHolder holder, int groupPosition);
}

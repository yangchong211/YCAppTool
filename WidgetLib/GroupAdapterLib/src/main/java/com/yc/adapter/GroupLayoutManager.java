package com.yc.adapter;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/9/18
 *     desc  : 为分组列表提供的GridLayoutManager
 *     revise: 因为分组列表如果要使用GridLayoutManager实现网格布局。要保证组的头部和尾部是要单独占用一行的。
 *             否则组的头、尾可能会跟子项混着一起，造成布局混乱。
 *             自定义layoutManager，https://github.com/yangchong211/YCGroupAdapter
 * </pre>
 */
public class GroupLayoutManager extends GridLayoutManager {

    private AbsGroupAdapter mAdapter;

    public GroupLayoutManager(Context context, int spanCount,
                              AbsGroupAdapter adapter) {
        super(context, spanCount);
        mAdapter = adapter;
        setSpanSizeLookup();
    }

    private void setSpanSizeLookup() {
        super.setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int count = getSpanCount();
                if (mAdapter != null) {
                    int type = mAdapter.judgeType(position);
                    //只对子项做Grid效果，让footer接在child之后
                    if (type == AbsGroupAdapter.TYPE_CHILD || type == AbsGroupAdapter.TYPE_FOOTER) {
                        int groupPosition = mAdapter.getGroupPositionForPosition(position);
                        int childPosition = mAdapter.getChildPositionForPosition(groupPosition, position);
                        return getChildSpanSize(groupPosition, childPosition);
                    }
                }
                return count;
            }
        });
    }

    /**
     * 提供这个方法可以使外部改变子项的SpanSize。
     * 这个方法的作用跟{@link SpanSizeLookup#getSpanSize(int)}一样。
     * @param groupPosition
     * @param childPosition
     * @return
     */
    public int getChildSpanSize(int groupPosition, int childPosition) {
        return 1;
    }

    @Override
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {

    }
}
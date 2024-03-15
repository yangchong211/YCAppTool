package com.yc.baseclasslib.viewpager2;

import androidx.viewpager2.widget.ViewPager2;


/**
 * <pre>
 *     @author 杨充
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2019/01/30
 *     desc  : ViewPager2的滑动监听callback
 *     revise:
 * </pre>
 */
public class OnPageChangeCallback extends ViewPager2.OnPageChangeCallback {

    /**
     * 当前页面开始滑动时
     *
     * @param position             索引
     * @param positionOffset       滑动偏移
     * @param positionOffsetPixels 偏移像素
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    /**
     * 当前页面滑动状态变动时
     * 缓慢拖拽-> SCROLL_STATE_DRAGGING     1，开始滑动
     * 快速滚动-> SCROLL_STATE_SETTLING     2，滑动结束，滚动监听
     * 空闲状态-> SCROLL_STATE_IDLE         0，什么都没做
     *
     * @param state 状态
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        super.onPageScrollStateChanged(state);
    }

    /**
     * 当页面被选中时
     *
     * @param position 索引
     */
    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
    }

}

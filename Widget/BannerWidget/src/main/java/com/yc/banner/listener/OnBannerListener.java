package com.yc.banner.listener;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/6/20
 *     desc  : 自定义轮播图点击事件
 *     revise:
 * </pre>
 */
public interface OnBannerListener {
    /**
     * 轮播图的点击事件处理
     * @param position              索引
     */
    void OnBannerClick(int position);
}

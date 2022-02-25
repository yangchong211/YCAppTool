package com.yc.animation.inter;

/**
 * 自定义估值器类型定义，主要用来辅助定义 缓动函数(http://easings.net/zh-cn) 的各种效果
 * @author 杨充
 * @date 2017/5/26 15:00
 */
public interface IEasingFunction<T> {
    T bounceEaseOut();
    T elasticEaseOut();
}

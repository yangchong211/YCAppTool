package com.yc.keyeventlib.board;

import android.content.Context;
import android.view.InputDevice;
import android.view.KeyEvent;

import com.yc.keyeventlib.code.IKeyEvent;

import java.util.HashMap;

/**
 * 键盘分发相关接口
 */
public interface IDispatchEvent {

    /**
     * 初始化，为了注册键盘发现广播
     *
     * @param context 上下文
     */
    void init(Context context);

    /**
     * 键盘事件分发
     *
     * @param keyCode 键值
     * @param event 事件
     * @return 是否消费
     */
    boolean dispatchKeyEvent(int keyCode, KeyEvent event);

    /**
     * 获取按键事件
     *
     * @return 按键的实现类
     */
    IKeyEvent getKeyEvent();

    /**
     * 处理外接键盘转换事件，针对于数字按键转导航等逻辑
     *
     * @param keyEvent 按键事件
     * @param enableNumToNavigation 设置是否允许数字转成上下导航
     * @return 新的实现类
     */
    IKeyEvent handleAttachKeyEvent(IKeyEvent keyEvent, boolean enableNumToNavigation);

    /**
     * 添加拦截器
     *
     * @param attachCallback 拦截器
     */
    void addAttachCallback(ResultCallback<HashMap<Integer, InputDevice>> attachCallback);

    /**
     * 删除拦截器
     *
     * @param attachCallback 拦截器
     */
    void rmAttachCallback(ResultCallback<HashMap<Integer, InputDevice>> attachCallback);

    /**
     * 添加按键回调
     *
     * @param keyEventCallback 拦截器
     */
    void addDispatchCallback(ResultCallback<IKeyEvent> keyEventCallback);

    /**
     * 移除按键回调
     *
     * @param keyEventCallback 拦截器
     */
    void removeDispatchCallback(ResultCallback<IKeyEvent> keyEventCallback);

    /**
     * 解除初始化，为了解除键盘发现广播
     */
    void deInit();
}

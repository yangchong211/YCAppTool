package com.yc.animation.model;

/**
 * 记录动画 view 的属性名称及对应的目标值，在降级处理中使用
 * @author 杨充
 * @date 2017/5/27 12:21
 */
public class TargetValueHolder {
    private String propertyName;
    private float value;

    private TargetValueHolder(String propertyName, float value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public static TargetValueHolder create(String propertyName, float value) {
        return new TargetValueHolder(propertyName, value);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public float getValue() {
        return value;
    }
}

package com.yc.animation.model;


/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time  :  2017/5/26
 *     desc  :  动画属性值统一存于此类中，在不同的动画实现方案中自行转换为需要的类型
 *     revise:
 * </pre>
 */
public final class AnimationPropertyValuesHolder {

    private String propertyName;
    private float[] values;

    private AnimationPropertyValuesHolder(String propertyName, float... values) {
        this.propertyName = propertyName;
        this.values = values;
    }

    public static AnimationPropertyValuesHolder create(String propertyName, float... values) {
        return new AnimationPropertyValuesHolder(propertyName, values);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public float[] getValues() {
        return values;
    }
}

package com.yc.privacymonitor.bean;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAndroidTool
 *     email : yangchong211@163.com
 *     time  : 2021/05/09
 *     desc  : 方法包装类
 *     revise:
 * </pre>
 */
public final class ClassMethodBean {
    /**
     * 当前class类名称
     */
    private final String targetClassName;
    /**
     * 所有需要监测的风险函数
     */
    private final LinkedList<String> methodGroup = new LinkedList<>();

    public ClassMethodBean(String className) {
        this.targetClassName = className;
    }

    public void addMethod(String name){
        methodGroup.add(name);
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public LinkedList<String> getMethodGroup() {
        return methodGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassMethodBean that = (ClassMethodBean) o;
        return equals(targetClassName, that.targetClassName);
    }

    @Override
    public int hashCode() {
        return hash(targetClassName);
    }

    private boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    private int hash(Object... values) {
        return Arrays.hashCode(values);
    }
}


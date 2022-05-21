package com.sankuai.erp.component.appinit.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者:王浩
 * 创建时间:2018/1/18
 * 描述:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface AppInit {
    Process process() default Process.MAIN; // 指定在哪个进程初始化

    int priority(); // 模块内部初始化顺序的优先级，越大越先初始化

    String aheadOf() default ""; // 在指定项之前初始化，用于整个项目范围内重新排序

    String description() default ""; // 描述

    boolean onlyForDebug() default false; // 只有在 debug 时才初始化
}

package com.ns.yc.lifehelper.ui.test.eventBus;

import org.greenrobot.eventbus.ThreadMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by PC on 2017/11/14.
 * 事件接收函数的注解类，运行在函数上的
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscriber {

    ThreadMode threadMode() default ThreadMode.POSTING;

    boolean sticky() default false;

    int priority() default 0;

}

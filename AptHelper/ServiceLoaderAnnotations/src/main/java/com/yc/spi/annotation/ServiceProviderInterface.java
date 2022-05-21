package com.yc.spi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @time : 2021/04/15
 * @desc : 接口注解类
 * @revise :
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceProviderInterface {
}

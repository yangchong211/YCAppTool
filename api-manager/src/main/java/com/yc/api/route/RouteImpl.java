package com.yc.api.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/12/12
 *     desc  : 路由
 *     revise: 修饰接口实现类
 * </pre>
 */
//@Retention用来修饰这是一个什么类型的注解。这里表示该注解是一个编译时注解。
@Retention(RetentionPolicy.CLASS)
//@Target用来表示这个注解可以使用在哪些地方。比如：类、方法、属性、接口等等。
//这里ElementType.TYPE 表示这个注解可以用来修饰：Class, interface or enum declaration。
@Target(ElementType.TYPE)
public @interface RouteImpl {

    /**
     * 修饰的类
     * @return
     */
    Class value();
}

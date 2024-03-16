package com.yc.toolutils.reflect;

import java.lang.reflect.Constructor;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 获取class对象的构造函数
 *     revise: 之前搜车封装库
 *
 * </pre>
 */
public final class ConstructorUtils {

    /**
     * Constructor<?>[] allConstructors = class.getDeclaredConstructors();//获取class对象的所有声明构造函数
     * Constructor<?>[] publicConstructors = class.getConstructors();//获取class对象public构造函数
     * Constructor<?> constructor = class.getDeclaredConstructor(String.class);//获取指定声明构造函数
     * Constructor publicConstructor = class.getConstructor(String.class);//获取指定声明的public构造函数
     */
    private ConstructorUtils(){

    }

    /**
     * 获取class对象public构造函数
     * @param cls           cls
     * @return
     */
    public static Constructor<?>[] getConstructors(Class<?> cls){
        return cls.getConstructors();
    }

    /**
     * 获取class对象public指定声明构造函数
     * @param cls           cls
     * @return
     */
    public static Constructor<?> getConstructor(Class<?> cls , Class<?>... parameterTypes)
            throws NoSuchMethodException {
        return cls.getConstructor(parameterTypes);
    }

    /**
     * 获取class对象的所有声明构造函数【包含私有构造】
     * @param cls           cls
     * @return
     */
    public static Constructor<?>[] getDeclaredConstructors(Class<?> cls){
        return cls.getDeclaredConstructors();
    }

    /**
     * 获取class对象的指定声明构造函数【包含私有构造】
     * @param cls           cls
     * @return
     */
    public static Constructor<?> getDeclaredConstructor(Class<?> cls, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        return cls.getDeclaredConstructor(parameterTypes);
    }



}

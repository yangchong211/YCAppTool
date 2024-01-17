package com.yc.designbusiness.creational;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 单例模式
 *     revise:
 * </pre>
 */
public class DesignInstance {

    /**
     * 单例模式
     *
     * 饿汗式
     * 懒汉式
     * DCL模式
     * 静态内部类
     * 枚举单例
     */
    private static volatile DesignInstance instance;

    /**
     * 单例特点
     * 1.构造私有
     * 2.通过静态获取对象
     * 3.对象只有一个(多线程)
     * 4.
     */
    private DesignInstance(){

    }

    /**
     * 饿汉式
     *
     */
    private static final DesignInstance INSTANCE = new DesignInstance();
    public static synchronized DesignInstance getInstance1(){
        return INSTANCE;
    }


    /**
     * 懒汉式
     * @return
     */
    public static DesignInstance getInstance2(){
        if (instance == null){
            instance = new DesignInstance();
        }
        return instance;
    }

    /**
     * 懒汉式，加锁
     * @return
     */
    public static synchronized DesignInstance getInstance3(){
        if (instance == null){
            instance = new DesignInstance();
        }
        return instance;
    }

    /**
     * DCL
     * @return
     */
    public static synchronized DesignInstance getInstance4(){
        //避免不必要的同步
        if (instance == null){
            synchronized (DesignInstance.class){
                if (instance == null){
                    instance = new DesignInstance();
                }
            }
        }
        return instance;
    }

    /**
     * 静态内部类
     * @return
     */
    public static synchronized DesignInstance getInstance5(){
        return Loader.INSTANCE;
    }

    public static class Loader{
        private static final DesignInstance INSTANCE = new DesignInstance();
    }


    public static Singleton getInstance6(){
        return Singleton.INSTANCE;
    }

    /**
     * enum枚举类
     */
    public enum Singleton {
        /**
         * 单例
         */
        INSTANCE;

        public void whateverMethod() {

        }
    }
}

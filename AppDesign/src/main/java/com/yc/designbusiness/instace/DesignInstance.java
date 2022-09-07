package com.yc.designbusiness.instace;

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
    public static synchronized DesignInstance getInstance4(){
        return INSTANCE;
    }


    /**
     * 懒汉式
     * @return
     */
    public static DesignInstance getInstance1(){
        if (instance == null){
            instance = new DesignInstance();
        }
        return instance;
    }

    /**
     * 懒汉式，加锁
     * @return
     */
    public static synchronized DesignInstance getInstance2(){
        if (instance == null){
            instance = new DesignInstance();
        }
        return instance;
    }

    /**
     * DCL
     * @return
     */
    public static synchronized DesignInstance getInstance3(){
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
    public static synchronized DesignInstance getInstance6(){
        return Loader.INSTANCE;
    }

    public static class Loader{
        private static final DesignInstance INSTANCE = new DesignInstance();
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

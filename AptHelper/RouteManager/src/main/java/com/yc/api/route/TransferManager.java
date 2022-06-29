package com.yc.api.route;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/12/12
 *     desc  : Transfer类
 *     revise: get/put操作，其中put操作自己完成
 * </pre>
 */
public class TransferManager implements IRegister {

    private volatile static TransferManager instance;
    /**
     * key表示的是自定义通信接口
     * value表示自定义通信接口的实现类
     */
    private final Map<Class, Class> apiImplementMap = new HashMap<>();

    private TransferManager() {
        //只允许自己内部构造
    }

    public static TransferManager getInstance() {
        if (instance == null) {
            synchronized (TransferManager.class) {
                if (instance == null) {
                    instance = new TransferManager();
                }
            }
        }
        return instance;
    }

    @Override
    public <I extends IRoute, E extends I> void register(Class<I> apiInterface, Class<E> apiImplement) {
        String interfaceName = apiInterface.getName();
        String implementName = apiImplement.getName();
        if (RouteConstants.LOG){
            System.out.println("interface-----interfaceName--put-"+interfaceName);
            System.out.println("interface-----implementName--put-"+implementName);
        }
        apiImplementMap.put(apiInterface, apiImplement);
    }

    public void clear() {
        //清除集合
        apiImplementMap.clear();
    }

    /**
     * 使用动态代理
     * @param tClass    当前class
     * @param <T>       T
     * @return          返回T
     */
    public <T extends IRoute> T getApi(Class<T> tClass) {
        Class<? extends T> implementClass = apiImplementMap.get(tClass);
        if (RouteConstants.LOG){
            System.out.println("interface-----implementClass--get-"+implementClass);
        }
        if (implementClass == null) {
            try {
                String simpleName = tClass.getSimpleName();
                String name = RouteConstants.PACKAGE_NAME_CONTRACT + "." + simpleName +
                        RouteConstants.SEPARATOR + RouteConstants.CONTRACT;
                if (RouteConstants.LOG){
                    System.out.println("interface-----TransferManager--get-"+name);
                }
                //通过反射去创建对象
                Class<?> aClass = Class.forName(name);
                //Object o = aClass.newInstance();
                //该种方式只能反射那种带有无参构造方法的类，否则创建会失败……
                Constructor<?> constructor = aClass.getConstructor();
                //创建对象
                //使用newInstance()方法的时候，就必须保证：1、这个类已经加载；2、这个类已经连接了。
                //完成上面两个步骤的正是Class的静态方法forName()所完成的，这个静态方法调用了启动类加载器，即加载java API的那个加载器。
                Object newInstance = constructor.newInstance();
                IRouteContract routeContract = (IRouteContract) newInstance;
                //直接注册，IRouteContract是接口，具体看实现它的实现类。实现类，其实就是注解apt生成的代码
                //调用register方法，是想将接口和接口实现类的class对象存储到map集合中
                routeContract.register(this);
                //然后再去获取key对应的value，也就是接口的实现类
                implementClass = apiImplementMap.get(tClass);
            } catch (Exception e) {
                e.printStackTrace();
                if (RouteConstants.LOG){
                    System.out.println("interface-----反射出现异常---"+e.getMessage());
                }
            }
        }
        //获取反射的clazz
        if (implementClass != null) {
            try {
                //创建对象
                return implementClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                if (RouteConstants.LOG){
                    System.out.println("interface-----创建对象出现异常---"+e.getMessage());
                }
            }
        }
        //获取classLoader
        ClassLoader classLoader = tClass.getClassLoader();
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                }
                if (RouteConstants.LOG){
                    System.out.println("interface-----创建对象出现异常---"+method.getName());
                }
                if ("isPresent".equals(method.getName())) {
                    return false;
                }
                if (method.getReturnType() == void.class) {
                    return null;
                }
                throw new IllegalStateException("空对象不能调用非空对象的方法" +
                        " 返回void或它的名字不是isPresent。请先用isPresent()");
            }
        };
        Class<?>[] classes = new Class[]{tClass};
        //使用
        Object proxyInstance = Proxy.newProxyInstance(classLoader, classes, invocationHandler);
        if (RouteConstants.LOG){
            System.out.println("interface-----proxyInstance---"+proxyInstance);
        }
        return (T) proxyInstance;
    }
}

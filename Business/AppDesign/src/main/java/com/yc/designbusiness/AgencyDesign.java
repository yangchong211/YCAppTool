package com.yc.designbusiness;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 代理设计模式学习
 *     revise :
 *     GitHub : https://github.com/yangchong211
 * </pre>
 */
public class AgencyDesign {

    /*-------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------静态代理---------------------------------*/
    /*-------------------------------------------------------------------------------------------*/

    /**
     * 静态代理伪代码
     */
    private void test1() {
        //1.创建委托对象
        RealSubject subject = new RealSubject();
        //2.创建调用处理器对象
        MyProxy1 p = new MyProxy1(subject);
        //3.通过代理对象调用方法
        p.request();


        new MyProxy1(new RealSubject2()).request();
        new MyProxy1(new RealSubject3()).request();

        //代理2
        new MyProxy2().request();
    }

    /**
     * 代理类和委托类会实现接口
     */
    interface Subject{
        void request();
    }

    /**
     * 委托类
     */
    static class RealSubject implements Subject{
        @Override
        public void request(){
            System.out.println("request");
        }
    }

    static class RealSubject2 implements Subject{
        @Override
        public void request(){
            System.out.println("request2");
        }
    }


    static class RealSubject3 implements Subject{
        @Override
        public void request(){
            System.out.println("request3");
        }
    }

    /**
     * 代理1
     */
    static class MyProxy1 implements Subject{
        private final Subject subject;

        public MyProxy1(Subject subject){
            this.subject = subject;
        }
        @Override
        public void request(){
            subject.request();
        }
    }

    /**
     * 代理2
     */
    static class MyProxy2 implements Subject{

        private final Subject subject;

        public MyProxy2(){
            subject = new RealSubject();
        }
        @Override
        public void request(){
            subject.request();
        }
    }


    /*-------------------------------------------------------------------------------------------*/
    /*------------------------------------------------动态代理------------------------------*/
    /*-------------------------------------------------------------------------------------------*/

    /**
     * 动态代理伪代码
     */
    private void test2() {
        //1.创建委托对象
        ISubject subject = new RealSubjectImpl1();
        ISubject realSubject2 = new RealSubjectImpl1();
        ISubject realSubject3 = new RealSubjectImpl1();



        //2.创建调用处理器对象
        ProxyHandler handler = new ProxyHandler(subject);
        //3.动态生成代理对象
        Subject proxySubject = (Subject) Proxy.newProxyInstance(
                subject.getClass().getClassLoader(),
                subject.getClass().getInterfaces(), handler);
        //4.通过代理对象调用方法
        proxySubject.request();
    }

    interface ISubject{
        void request();
    }

    static class RealSubjectImpl1 implements ISubject{
        @Override
        public void request(){
            System.out.println("request real1");
        }
    }

    static class RealSubjectImpl2 implements ISubject{
        @Override
        public void request(){
            System.out.println("request real2");
        }
    }

    static class RealSubjectImpl3 implements ISubject{
        @Override
        public void request(){
            System.out.println("request real3");
        }
    }

    /**
     * 代理类的调用处理器
     */
    static class ProxyHandler implements InvocationHandler {
        private final ISubject subject;
        public ProxyHandler(ISubject subject){
            this.subject = subject;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //定义预处理的工作，当然你也可以根据 method 的不同进行不同的预处理工作
            System.out.println("====before====");
            Object result = method.invoke(subject, args);
            System.out.println("====after====");
            return result;
        }
    }

}

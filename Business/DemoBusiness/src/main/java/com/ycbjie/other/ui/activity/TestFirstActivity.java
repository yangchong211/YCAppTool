package com.ycbjie.other.ui.activity;

import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.other.R;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestFirstActivity extends BaseActivity implements View.OnClickListener {

    private int a;
    private volatile int b;

    @Override
    public int getContentView() {
        return R.layout.activity_test_first;
    }

    @Override
    public void initView() {
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
        findViewById(R.id.tv_5).setOnClickListener(this);
        findViewById(R.id.tv_6).setOnClickListener(this);
        findViewById(R.id.tv_7).setOnClickListener(this);
        findViewById(R.id.tv_8).setOnClickListener(this);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_1) {
            ToastUtils.showRoundRectToast("测试普通数据在多线程下数据");
            test1();
        } else if (i == R.id.tv_2) {
            ToastUtils.showRoundRectToast("测试volatile数据在多线程下数据");
            test2();
        } else if (i == R.id.tv_3) {
            ToastUtils.showRoundRectToast("测试多线程下volatile机制");
            test3();
        } else if (i == R.id.tv_4) {
            ToastUtils.showRoundRectToast("测试多线程下volatile机制");
            test4();
        } else if (i == R.id.tv_5) {
            test5();
        } else if (i == R.id.tv_6){
            test6();
        } else if (i == R.id.tv_7){
            test7();
        } else if (i == R.id.tv_8){
            test8();
        }
    }


    /**
     * 测试volatile
     */
    private void test1(){
        for (int i=0 ; i<100 ; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    a++;
                    LogUtils.e("测试普通数据"+a);
                }
            }).start();
        }
    }

    /**
     * 测试volatile
     */
    private void test2(){
        for (int i=0 ; i<100 ; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    b++;
                    LogUtils.e("测试volatile数据"+b);
                }
            }).start();
        }
    }


    private void test3() {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                new VolatileExample().writer();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                new VolatileExample().reader();
            }
        });
        thread1.start();
        thread2.start();
    }

    public class VolatileExample {
        private int a = 0;
        private volatile boolean flag = false;
        public void writer(){
            a = 1;          //1
            LogUtils.e("测试volatile数据1--"+a);
            flag = true;   //2
            LogUtils.e("测试volatile数据2--"+flag);
        }
        public void reader(){
            LogUtils.e("测试volatile数据3--"+flag);
            if(flag){      //3
                int i = a; //4
                LogUtils.e("测试volatile数据4--"+i);
            }
        }
    }


    private static volatile boolean isOver = false;
    private void test4(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isOver) {
                    LogUtils.e("测试volatile数据"+isOver);
                }
            }
        });
        thread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isOver = true;
    }


    private volatile int c = 0;
    private void test5(){
        for (int x=0 ; x<=100 ; x++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    c++;
                    LogUtils.e("小杨逗比Thread-------------"+c);
                }
            }).start();
        }
    }


    /**
     * 静态代理伪代码
     */
    private void test6() {
        //1.创建委托对象
        RealSubject subject = new RealSubject();
        //2.创建调用处理器对象
        MyProxy p = new MyProxy(subject);
        //3.通过代理对象调用方法
        p.request();
    }

    /**
     * 动态代理伪代码
     */
    private void test7() {
        //1.创建委托对象
        RealSubject realSubject = new RealSubject();
        //2.创建调用处理器对象
        ProxyHandler handler = new ProxyHandler(realSubject);
        //3.动态生成代理对象
        Subject proxySubject = (Subject)Proxy.newProxyInstance(
                RealSubject.class.getClassLoader(),
                RealSubject.class.getInterfaces(), handler);
        //4.通过代理对象调用方法
        proxySubject.request();
    }

    interface Subject{
        void request();
    }

    class RealSubject implements Subject{
        @Override
        public void request(){
            System.out.println("request");
        }
    }

    /**
     * 静态代理
     */
    class MyProxy implements Subject{
        private Subject subject;
        public MyProxy(Subject subject){
            this.subject = subject;
        }
        @Override
        public void request(){
            System.out.println("PreProcess");
            subject.request();
            System.out.println("PostProcess");
        }
    }


    /**
     * 代理类的调用处理器
     */
    class ProxyHandler implements InvocationHandler {
        private Subject subject;
        public ProxyHandler(Subject subject){
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



    private void test8() {
        localVarGc1();   // 没有GC
//        localVarGc2();   // GC
//        localVarGc3();   // 没有GC
//        localVarGc4();   // GC
//        localVarGc5();   // GC
    }

    private static final int SIZE = 6 * 1024 * 1024;
    public static void localVarGc1() {
        byte[] b = new byte[SIZE];
        System.gc();
    }
    public static void localVarGc2() {
        byte[] b = new byte[SIZE];
        b = null;
        System.gc();
    }
    public static void localVarGc3() {
        {
            byte[] b = new byte[SIZE];
        }
        System.gc();
    }
    public static void localVarGc4() {
        {
            byte[] b = new byte[SIZE];
        }
        int c = 0;
        System.gc();
    }
    public static void localVarGc5() {
        localVarGc1();
        System.gc();
    }


    private void hashMap(){
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("语文", 1);
        map.put("数学", 2);
        map.put("英语", 3);
        map.put("历史", 4);
        map.put("政治", 5);
        map.put("地理", 6);
        map.put("生物", 7);
        map.put("化学", 8);

        //第一种方式，for each 遍历
        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println( key + ": " + value);
        }
        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            //通过key找键，效率相比比较低
            Integer value = map.get(key);
            System.out.println( key + ": " + value);
        }

        //第二种方式，使用迭代器
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer> next = iterator.next();
            String key = next.getKey();
            Integer value = next.getValue();
            System.out.println(key + ": " + value);
        }
        while (iterator.hasNext()){
            Map.Entry<String, Integer> next = iterator.next();
            String key = next.getKey();
            //通过key找键，效率相比比较低
            Integer value = map.get(key);
            System.out.println(key + ": " + value);
        }

    }



}

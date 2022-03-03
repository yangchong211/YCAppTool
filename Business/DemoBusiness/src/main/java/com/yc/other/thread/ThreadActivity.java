package com.yc.other.thread;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.other.R;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_demo);
        findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test1();
            }
        });
        findViewById(R.id.tv_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test2();
            }
        });
        findViewById(R.id.tv_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test3();
            }
        });
        findViewById(R.id.tv_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test4();
            }
        });
        findViewById(R.id.tv_51).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test51();
            }
        });
        findViewById(R.id.tv_52).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test52();
            }
        });
        findViewById(R.id.tv_6_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test6_1();
            }
        });
        findViewById(R.id.tv_6_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test6_2();
            }
        });
        findViewById(R.id.tv_7_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test7_1();
            }
        });
        findViewById(R.id.tv_8_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test8_1();
            }
        });
        findViewById(R.id.tv_8_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test8_2();
            }
        });
        findViewById(R.id.tv_9_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test9_1();
            }
        });
    }

    private void test2() {
        // 创建3个线程对象
        SellTicktes2 t1 = new SellTicktes2() ;
        SellTicktes2 t2 = new SellTicktes2() ;
        SellTicktes2 t3 = new SellTicktes2() ;
        // 设置名称
        t1.setName("窗口1") ;
        t2.setName("窗口2") ;
        t3.setName("窗口3") ;
        // 启动线程
        t1.start() ;
        t2.start() ;
        t3.start() ;
    }

    public static class SellTicktes2 extends Thread {
        private static int num = 100 ;
        @Override
        public void run() {
            // 模拟售票
            while(true) {
                if( num > 0 ) {
                    try {
                        Thread.sleep(100) ;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("test2"+Thread.currentThread().getName() + "正在出售" + (num--) + "张票");
                }
            }
        }
    }

    private void test1() {
        /**
         * 需求：某电影院目前正在上映贺岁大片，共有100张票，而它有3个售票窗口售票，请设计一个程序模拟该电影院售票。
         */
        // 创建3个线程对象
        SellTicktes t1 = new SellTicktes() ;
        SellTicktes t2 = new SellTicktes() ;
        SellTicktes t3 = new SellTicktes() ;
        // 设置名称
        t1.setName("窗口1") ;
        t2.setName("窗口2") ;
        t3.setName("窗口3") ;
        // 启动线程
        t1.start() ;
        t2.start() ;
        t3.start() ;
    }

    public static class SellTicktes extends Thread {
        private static int num = 100 ;
        @Override
        public void run() {
            /**
             * 定义总票数
             *
             * 如果我们把票数定义成了局部变量,那么表示的意思是每一个窗口出售了各自的100张票; 而我们的需求是: 总共有100张票
             * 而这100张票要被3个窗口出售; 因此我们就不能把票数定义成局部变量,只能定义成成员变量
             */
            // 模拟售票
            while(true) {
                if( num > 0 ) {
                    System.out.println("test1"+Thread.currentThread().getName() + "正在出售" + (num--) + "张票");
                }
            }
        }
    }


    /**
     * 现在有T1、T2、T3三个线程，你怎样保证T2在T1执行完后执行，T3在T2执行完后执行？
     */
    private void test4(){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("test4"+Thread.currentThread().getName() + "线程执行" + "Thread1");
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("test4"+Thread.currentThread().getName() + "线程执行" + "Thread2");
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("test4"+Thread.currentThread().getName() + "线程执行" + "Thread3");
            }
        });

        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t3.start();
        try {
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void test3(){
        final ShareThread sh = new ShareThread();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    sh.Test01();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "T1").start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    sh.Test02();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "T2").start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    sh.Test03();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "T3").start();
    }


    class ShareThread {

        // flag作为标记
        private int flag = 1;
        private Lock lock = new ReentrantLock();
        private Condition c1 = lock.newCondition();
        private Condition c2 = lock.newCondition();
        private Condition c3 = lock.newCondition();

        public void Test01() throws InterruptedException {
            lock.lock();
            try {
                while (flag != 1) {
                    c1.await();
                }
                System.out.println("正在执行的是:" + Thread.currentThread().getName());
                flag = 2;
                c2.signal();// 通知一个线程来执行
            } finally {
                lock.unlock();
            }
        }

        public void Test02() throws InterruptedException {
            lock.lock();
            try {
                while (flag != 2) {
                    c2.await();
                }
                System.out.println("正在执行的是:" + Thread.currentThread().getName());
                flag = 3;
                c3.signal();// 通知一个线程来执行
            } finally {
                lock.unlock();
            }
        }

        public void Test03() throws InterruptedException {
            lock.lock();
            try {
                while (flag != 3) {
                    c3.await();
                }
                System.out.println("正在执行的是:" + Thread.currentThread().getName());
                flag = 1;
                c1.signal();// 通知一个线程来执行
            } finally {
                lock.unlock();
            }
        }
    }



    /**-----------------------------------测试volatile关键字---------------------------------------------*/

    static int i = 10;
    public void test51(){
//        MyThread51 t1 = new MyThread51();
//        MyThread51 t2 = new MyThread51();
//        t1.start() ;
//        try {
//            t1.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        t2.start() ;


        for (int i=0 ; i<10 ; i++){
            MyThread51 t1 = new MyThread51();
            t1.start() ;
        }
    }

    static class MyThread51 extends Thread {
        @Override
        public void run() {
            i++;
            System.out.println("yc---------" + this.getName()+"----"+i);
        }
    }


    static volatile int j = 10;

    public void test52(){
        for (int i=0 ; i<10 ; i++){
            MyThread52 t1 = new MyThread52();
            t1.start() ;
        }
    }

    static class MyThread52 extends Thread {
        @Override
        public void run() {
            j++;
            System.out.println("yc---------" + this.getName()+"----"+j);
        }
    }

    private void test6_1(){
        CallableThread ctt = new CallableThread();
        FutureTask<Integer> ft = new FutureTask<>(ctt);
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " 的循环变量i的值" + i);
            if (i == 20) {
                new Thread(ft, "有返回值的线程").start();
            }
        }
        try {
            System.out.println("子线程的返回值：" + ft.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void test6_2(){
        CallableThread ctt = new CallableThread();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        Future<Integer> submit = null;
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " 的循环变量i的值" + i);
            if (i == 20) {
                submit = fixedThreadPool.submit(ctt);
            }
        }
        try {
            if (submit!=null){
                System.out.println("子线程的返回值：" + submit.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public class CallableThread implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            int i = 0;
            for (; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + " " + i);
            }
            return i;
        }
    }

    private void test7_1() {
        // 创建对象
        MyThread t1 = new MyThread() ;
        // 启动线程: 需要使用start方法启动线程, 如果我们在这里调用的是run方法,那么我们只是把该方法作为普通方法进行执行
        //		t1.run() ;
        t1.start() ;		// 告诉jvm开启一个线程调用run方法
         t1.start() ;		// 一个线程只能被启动一次
        try {
            //让当前线程sleep一下，不会释放对象锁，当前线程等待
            Thread.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("小杨逗比");
        }
    }


    private void test8_1() {
        Object co = new Object();
        System.out.println(co);
        for (int i = 0; i < 5; i++) {
            MyThread2 t = new MyThread2("Thread" + i, co);
            t.start();
        }
        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("-----Main Thread notify-----");
            synchronized (co) {
                co.notify();
            }
            TimeUnit.SECONDS.sleep(2);
            System.out.println("Main Thread is end.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void test8_2() {
        Object co = new Object();
        System.out.println(co);
        for (int i = 0; i < 5; i++) {
            MyThread2 t = new MyThread2("Thread" + i, co);
            t.start();
        }
        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("-----Main Thread notify-----");
            synchronized (co) {
                co.notifyAll();
            }
            TimeUnit.SECONDS.sleep(2);
            System.out.println("Main Thread is end.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class MyThread2 extends Thread {

        private String name;
        private Object co;

        public MyThread2(String name, Object o) {
            this.name = name;
            this.co = o;
        }

        @Override
        public void run() {
            System.out.println(name + " is waiting.");
            try {
                synchronized (co) {
                    co.wait();
                }
                System.out.println(name + " has been notified.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void test9_1() {
        try {
            ExecutorService exc = Executors.newCachedThreadPool();
            exc.execute(new ExceptionThread1());
            System.out.println("主线程是可以执行到这的 只是出错的子线程被干掉了");
        } catch (Exception e) {
            System.err.println("捕获到异常了");
        }

        HashMap<String,String> map = new HashMap<>();
        map.put("1","doubi");
        String s = map.get("1");
    }

    public class ExceptionThread1 implements Runnable {
        @Override
        public void run() {
            throw new RuntimeException();
        }
    }
}

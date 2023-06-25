package com.yc.apptool.thread;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.yc.apptool.R;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class ThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
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
        t2.start();
        t3.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

}

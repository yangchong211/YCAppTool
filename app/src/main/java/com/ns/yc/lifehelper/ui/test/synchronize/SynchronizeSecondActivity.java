package com.ns.yc.lifehelper.ui.test.synchronize;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ns.yc.lifehelper.R;

/**
 * Created by PC on 2017/11/13.
 * 作者：PC
 * 当一个线程访问object的一个synchronized(this)同步代码块时，
 * 另一个线程仍然可以访问该object中的非synchronized(this)同步代码块。
 */

public class SynchronizeSecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_setting);
        System.out.println("synchronized 关键字使用 \n" +"--------------------------");
        Thread t02A = new Thread(new Runnable() {
            @Override
            public void run() {
                method01();
            }
        },"A");
        Thread t02B = new Thread(new Runnable() {

            @Override
            public void run() {
                method02();
            }
        },"B");
        Thread t02C = new Thread(new Runnable() {
            @Override
            public void run() {
                method3();
            }
        },"C");
        t02A.start();
        t02B.start();
        t02C.start();
    }


    public void method01(){
        synchronized (this) {
            int i=0;
            while(i++ < 3){
                System.out.println(Thread.currentThread().getName() +":"+ i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void method02(){

        //第1种方式:当一个线程访问object的一个synchronized(this)同步代码块时，
        //另一个线程仍然可以访问该object中的非synchronized(this)同步代码块。
//      int j=0;
//      while(j++ < 3){
//          System.out.println(Thread.currentThread().getName() +":"+ j);
//          try {
//              Thread.sleep(1000);
//          } catch (InterruptedException e) {
//              e.printStackTrace();
//          }
//      }

        //第2种方式:当一个线程访问object的一个synchronized(this)同步代码块时，
        //其他线程对object中所有其它synchronized(this)同步代码块的访问将被阻塞。
        synchronized (this) {
            int j=0;
            while(j++ < 3){
                System.out.println(Thread.currentThread().getName() +":"+ j);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 当一个线程访问object的一个synchronized(this)同步代码块时，
     * 它就获得了这个object的对象锁。
     * 结果，其它线程对该object对象所有同步代码部分的访问都被暂时阻塞。
     */
    public synchronized void method3(){
        int k=0;
        while(k++ < 3){
            System.out.println(Thread.currentThread().getName() +":"+ k);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 运行结果
     synchronized 关键字使用
     --------------------------
     B:1
     B:2
     B:3
     C:1
     C:2
     C:3
     A:1
     A:2
     A:3
     */

}

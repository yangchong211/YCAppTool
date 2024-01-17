package com.yc.apptool.thread;

public class DeadLockDemo {

    private final Object obj1 = new Object();
    private final Object obj2 = new Object();

    /**
     * 可能发生死锁
     */
    private void test1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj1){
                    System.out.println("yc---Thread1 obj1");
                    synchronized (obj2){
                        System.out.println("yc---Thread1 obj2");
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj2){
                    System.out.println("yc---Thread2 obj2");
                    synchronized (obj1){
                        System.out.println("yc---Thread2 obj1");
                    }
                }
            }
        }).start();
    }

}

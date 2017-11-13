package com.ns.yc.lifehelper.ui.test.synchronize;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ns.yc.lifehelper.R;

/**
 * Created by PC on 2017/11/13.
 * 作者：PC
 */

public class SynchronizeThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_setting);
        final InnerObject innerObj = new InnerObject();
        System.out.println("synchronized 关键字使用 \n" +"--------------------------");
        Thread t03A = new Thread(new Runnable() {
            @Override
            public void run() {
                outerMethod01(innerObj);
            }
        },"A");
        Thread t03B = new Thread(new Runnable() {
            @Override
            public void run() {
                outerMethod02(innerObj);
            }
        },"B");
        t03A.start();
        t03B.start();
    }

    class InnerObject{

        /**
         * 内部类方法1
         */
        private void innerMethod01(){
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

        /**
         * 内部类方法2
         */
        private void innerMethod02(){
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
     * 外部类方法1
     */
    private void outerMethod01(InnerObject innerObj){
        synchronized (innerObj) {
            innerObj.innerMethod01();
        }
    }

    /**
     * 外部类方法2
     */
    private void outerMethod02(InnerObject innerObj){
        innerObj.innerMethod02();
    }


    /**
     执行结果
     synchronized 关键字使用
     --------------------------
     A:1
     B:1
     B:2
     A:2
     B:3
     A:3
     */

}

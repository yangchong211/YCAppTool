package com.ns.yc.lifehelper.ui.test.synchronize;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ns.yc.lifehelper.R;

/**
 * Created by PC on 2017/11/13.
 * 作者：PC
 */

public class SynchronizeFirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_setting);
        Thread01 t01 = new Thread01();
        System.out.println("synchronized 关键字使用 \n" +"--------------------------");
        Thread ta = new Thread(t01,"A");
        Thread tb = new Thread(t01,"B");
        ta.start();
        tb.start();
    }

    private class Thread01 implements Runnable{
        @Override
        public void run() {
            synchronized (this) {
                for(int i=0;i<3;i++){
                    System.out.println(Thread.currentThread().getName()+" synchronized loop "+i);
                }
            }
        }
    }

    /**
     * 运行结果
     synchronized 关键字使用
     --------------------------
     B synchronized loop 0
     B synchronized loop 1
     B synchronized loop 2
     A synchronized loop 0
     A synchronized loop 1
     A synchronized loop 2
     */

}

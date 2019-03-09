package com.ycbjie.other.ui.activity;

import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.other.R;

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

}

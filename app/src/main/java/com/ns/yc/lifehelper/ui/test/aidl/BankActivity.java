package com.ns.yc.lifehelper.ui.test.aidl;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.utils.animation.AnimatorUtils;


public class BankActivity extends AppCompatActivity {

    private IBank mBankBinder; //服务端的Server对象

    private TextView tv;

    //用于绑定Server的ServerConnection对象
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBankBinder = (IBank)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_aidl);

        tv = (TextView) findViewById(R.id.tv);

        Intent intent = new Intent(this,BankService.class);
        intent.setAction("com.ns.yc.lifehelper.ui.test.aidl.BankService");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        init(R.id.b1);
        init(R.id.b2);
        init(R.id.b3);
        init(R.id.b4);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    private void init(int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.b1:
                        tv.setText(mBankBinder.OpenAccount("yc", "123456"));
                        initFirstAnim();
                        break;
                    case R.id.b2:
                        tv.setText(mBankBinder.saveMoney(12345, "yc"));
                        initSecondAnim();
                        break;
                    case R.id.b3:
                        tv.setText(mBankBinder.tackMoney(123,"yc", "123456"));
                        initThirdAnim();
                        break;
                    case R.id.b4:
                        tv.setText(mBankBinder.closeAccount("yc", "123456"));
                        initFourAnim();
                        break;
                }
            }


        });
    }



    private void initFirstAnim() {
        Button b1 = (Button) findViewById(R.id.b1);
        ValueAnimator valueAnimator = AnimatorUtils.setValueAnimator(b1,0, 2, 2000, 500, 2);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {

            }
        });
    }


    private void initSecondAnim() {
        Button b2 = (Button) findViewById(R.id.b2);
        ObjectAnimator objectAnimator = AnimatorUtils.setObjectAnimator(b2, "alpha", 1, 0, 2000, 500);
        objectAnimator.start();
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ToastUtils.showShort("更新");
            }
        });
    }


    private void initThirdAnim() {
        Button b3 = (Button) findViewById(R.id.b3);
        @SuppressLint("ResourceType")
        Animator mAnim = AnimatorInflater.loadAnimator(this, R.anim.animator_1_0);
        mAnim.setTarget(b3);
        mAnim.start();
        mAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //动画开始时执行
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束时执行
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //动画取消时执行
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //动画重复时执行
            }
        });
    }

    private void initFourAnim() {
        Button b4 = (Button) findViewById(R.id.b4);
        @SuppressLint("ResourceType")
        Animator mAnim2 = AnimatorInflater.loadAnimator(this, R.anim.animator_set);
        mAnim2.setTarget(b4);
        mAnim2.start();
        mAnim2.addListener(new AnimatorListenerAdapter() {
            // 向addListener()方法中传入适配器对象AnimatorListenerAdapter()
            // 由于AnimatorListenerAdapter中已经实现好每个接口
            // 所以这里不实现全部方法也不会报错
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                ToastUtils.showShort("动画结束了");
            }
        });
    }


}

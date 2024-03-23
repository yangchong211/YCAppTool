package com.yc.other.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;


import com.yc.library.base.mvp.BaseActivity;
import com.yc.other.R;

public class TestActivity extends BaseActivity implements View.OnClickListener {



    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, TestActivity.class);
            //target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public int getContentView() {
        return R.layout.activity_test_other;
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
        findViewById(R.id.tv_9).setOnClickListener(this);
        findViewById(R.id.tv_10).setOnClickListener(this);
        findViewById(R.id.tv_11).setOnClickListener(this);
        findViewById(R.id.tv_12).setOnClickListener(this);
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
        } else if (i == R.id.tv_2) {
            startActivity(new Intent(this, BannerViewActivity.class));
        } else if (i == R.id.tv_3) {
        } else if (i == R.id.tv_4) {
            startActivity(new Intent(this, ProgressThirdActivity.class));
        } else if (i == R.id.tv_5) {
        } else if (i == R.id.tv_6){
            startActivity(new Intent(this, TestFirstActivity.class));
        } else if (i == R.id.tv_7){
        } else if (i == R.id.tv_8){
        }else if (i == R.id.tv_9){
            startActivity(new Intent(this, CloneAbleActivity.class));
        }else if (i == R.id.tv_10){
        } else if (i == R.id.tv_12){
            startActivity(new Intent(this, SerialTaskActivity.class));
        }
    }

    private void test(){
        DelegateSubject delegateSubject = new DelegateSubject();
        delegateSubject.request();
    }


    public interface ISubject{
        void request();
    }

    public class SubjectImpl implements ISubject{
        @Override
        public void request() {
            System.out.println("request");
        }
    }

    public class DelegateSubject implements ISubject{

        private final ISubject subject = new SubjectImpl();

        @Override
        public void request() {
            subject.request();
        }
    }

}

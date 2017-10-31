package com.ns.yc.lifehelper.ui.test.observable;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ns.yc.lifehelper.R;

import java.util.ArrayList;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/3/31
 * 描    述：简单模拟观察者设计模式
 * 修订历史：
 * 测试
 * ================================================
 */
public class MeTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_1;
    private Button btn_2;
    private MeUser observable;
    private ArrayList<MeObserver> myObservers;
    private int i;
    private MeObserver myObserver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_observer);
        initTest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        observable.deleteObserver(myObserver);
    }

    private void initTest() {
        //被观察的角色
        observable = new MeUser();
        myObservers = new ArrayList<>();
        initView();
        initListener();
    }


    private void initView() {
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
    }

    private void initListener() {
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_1:                                //添加观察者
                MeUser user=new MeUser();
                MeObserver coder1=new MeObserver("name1");
                MeObserver coder2=new MeObserver("name2");
                MeObserver coder3=new MeObserver("name3");
                user.addObserver(coder1);
                user.addObserver(coder2);
                user.addObserver(coder3);
                user.postNewContentToCoder("contentChanged");
                break;
            case R.id.btn_2:                                //改变数据

                break;
        }
    }


}

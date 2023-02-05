package com.yc.appleet;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yc.appleet.array.ArrayTestActivity;
import com.yc.appleet.code.CodeTestActivity;
import com.yc.appleet.node.NodeTestActivity;
import com.yc.appleet.queue.QueueTestActivity;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.monitorphone.MonitorPhoneActivity;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 算法
 *     revise:
 * </pre>
 */
public class LeetCodeActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;
    private TextView tv9;
    private TextView tv10;
    private TextView tv11;
    private TextView tv12;

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, LeetCodeActivity.class);
            //target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_leet_test;
    }

    @Override
    public void initView() {
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);
        tv5 = findViewById(R.id.tv_5);
        tv6 = findViewById(R.id.tv_6);
        tv7 = findViewById(R.id.tv_7);
        tv8 = findViewById(R.id.tv_8);
        tv9 = findViewById(R.id.tv_9);
        tv10 = findViewById(R.id.tv_10);
        tv11 = findViewById(R.id.tv_11);
        tv12 = findViewById(R.id.tv_12);
    }

    @Override
    public void initListener() {
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == tv1){
            //算法导论
            startActivity(CodeTestActivity.class);
        } else if (v == tv2){
            //数组
            startActivity(ArrayTestActivity.class);
        } else if (v == tv3){
            //链表
            startActivity(NodeTestActivity.class);
        } else if (v == tv4){
            //栈

        } else if (v == tv5){
            //队列
            startActivity(QueueTestActivity.class);
        }
    }
}

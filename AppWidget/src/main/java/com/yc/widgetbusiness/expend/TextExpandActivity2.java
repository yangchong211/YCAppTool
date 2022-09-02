package com.yc.widgetbusiness.expend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.expandlib.ExpandLayout;
import com.yc.expandlib.ExpandLogUtils;
import com.yc.widgetbusiness.R;

/**
 * @author yc
 */
public class TextExpandActivity2 extends AppCompatActivity {

    private ExpandLayout expand;
    private TextView tv;
    private Button btn;
    private int measuredHeight;
    private static final int TAG_LAYOUT_FINISH = 521;
    private int twoLineTagHeight;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TAG_LAYOUT_FINISH:
                    //这个是两行的高度，实际开发中看UI设置的高度，这里仅仅是假设操作
                    twoLineTagHeight = dip2px(TextExpandActivity2.this, 54.0f);
                    if (measuredHeight> twoLineTagHeight){
                        //如果大于两行，则显示折叠
                        btn.setVisibility(View.VISIBLE);
                        expand.initExpand(false ,twoLineTagHeight);
                    }else {
                        //如果小于或者等于两行，则不显示折叠控件
                        btn.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_expand2);

        initView();
        initListener();
        initExpend();
    }


    private void initView() {
        expand = findViewById(R.id.expand);
        tv = findViewById(R.id.tv);
        btn = findViewById(R.id.btn);
    }


    private void initListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandLogUtils.d("点击事件");
                expand.toggleExpand();
            }
        });
        expand.setAnimationDuration(500);
        expand.setOnToggleExpandListener(new ExpandLayout.OnToggleExpandListener() {
            @Override
            public void onToggleExpand(boolean isExpand) {
                if (isExpand){
                    btn.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initExpend() {
        tv.post(new Runnable() {
            @Override
            public void run() {
                measuredHeight = tv.getMeasuredHeight();
                ExpandLogUtils.d("flowView--获取内容布局---" + measuredHeight);
                handler.sendEmptyMessage(TAG_LAYOUT_FINISH);
            }
        });
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

package com.yc.other.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.library.base.mvp.BaseActivity;
import com.yc.other.R;
import com.yc.ycprogresslib.NumberProgressbar;
import com.yc.ycprogresslib.OnNumberProgressListener;
import com.yc.ycprogresslib.ProgressBarUtils;

/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2016/2/10
 *     desc  : 自定义进度条，新芽，沙丘大学下载进度条
 *     revise: 参考案例：夏安明博客http://blog.csdn.net/xiaanming/article/details/10298163
 *             案例地址：<a href="https://github.com/yangchong211/YCProgress">...</a>
 * </pre>
 */
public class ProgressThirdActivity extends AppCompatActivity implements View.OnClickListener {

    private NumberProgressbar bar1;
    private NumberProgressbar bar2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_third);
        initView();
        initListener();
    }

    private void initView() {

        bar1 = findViewById(R.id.bar1);
        //设置倒计时总时间
        bar1.setTimeMillis(10000);
        //设置最大进度条的值
        bar1.setMax(100);
        //设置进度条文本的颜色
        bar1.setProgressTextColor(this.getResources().getColor(R.color.colorAccent));
        //设置进度条文本的大小
        bar1.setProgressTextSize(ProgressBarUtils.sp2px(this,14));
        //设置百分比文字内容是否可见
        bar1.setNumberTextVisibility(ProgressBarUtils.NumberTextVisibility.Visible);
        //设置百分比进度条的高度
        bar1.setReachedBarHeight(10);
        //设置未更新百分比进度条的高度
        bar1.setUnreachedBarHeight(10);
        //设置百分比进度条的颜色
        bar1.setReachedBarColor(this.getResources().getColor(R.color.redTab));
        //设置未更新百分比进度条的颜色
        bar1.setUnreachedBarColor(this.getResources().getColor(R.color.blackText2));
        //设置百分比进度条的监听
        bar1.setOnProgressBarListener(new OnNumberProgressListener() {
            @Override
            public void onProgressChange(int current, int max) {

            }
        });
        bar2 = findViewById(R.id.bar2);
        bar2.setTimeMillis(15000);

    }

    private void initListener() {
        findViewById(R.id.btn_11).setOnClickListener(this);
        findViewById(R.id.btn_12).setOnClickListener(this);
        findViewById(R.id.btn_13).setOnClickListener(this);
        findViewById(R.id.btn_21).setOnClickListener(this);
        findViewById(R.id.btn_22).setOnClickListener(this);
        findViewById(R.id.btn_23).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_11) {
            bar1.start();

        } else if (i == R.id.btn_12) {
            bar1.stop();

        } else if (i == R.id.btn_13) {
            bar1.reStart();

        } else if (i == R.id.btn_21) {
            bar2.start();

        } else if (i == R.id.btn_22) {
            bar2.stop();

        } else if (i == R.id.btn_23) {
            bar2.reStart();

        }
    }
}

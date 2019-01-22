package com.ycbjie.other.ui.activity;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ns.yc.ycprogresslib.ProgressBarUtils;
import com.ns.yc.ycprogresslib.RingProgressBar;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.other.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2016/2/10
 *     desc  : 自定义进度条，新芽，沙丘大学下载进度条
 *     revise: 参考案例：夏安明博客http://blog.csdn.net/xiaanming/article/details/10298163
 *             案例地址：https://github.com/yangchong211/YCProgress
 *             关于仿杀毒软件进度条控件
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_OTHER_PROGRESS1_ACTIVITY)
public class ProgressFirstActivity extends BaseActivity implements View.OnClickListener {


    private RingProgressBar bar_percent;
    private RingProgressBar bar_null;
    private RingProgressBar bar_all;


    private int max;
    private int progress1;
    private boolean isProgressGoing1;
    private Timer mTimer1;
    private TimerTask mTimerTask1;

    private int progress2;
    private boolean isProgressGoing2;
    private Timer mTimer2;
    private TimerTask mTimerTask2;


    private int progress3;
    private boolean isProgressGoing3;
    private Timer mTimer3;
    private TimerTask mTimerTask3;


    @Override
    public int getContentView() {
        return R.layout.activity_progress_first;
    }

    @Override
    public void initView() {
        max = 100;
        initBarPercent();
        initBarNull();
        initBarAll();
    }

    @Override
    public void initListener() {
        findViewById(R.id.btn_11).setOnClickListener(this);
        findViewById(R.id.btn_12).setOnClickListener(this);
        findViewById(R.id.btn_21).setOnClickListener(this);
        findViewById(R.id.btn_21).setOnClickListener(this);
        findViewById(R.id.btn_31).setOnClickListener(this);
        findViewById(R.id.btn_32).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_11) {
            if (progress1 == 100) {
                stopProgress1();
                progress1 = 0;
            }
            startProgress1();

        } else if (i == R.id.btn_12) {
            stopProgress1();

        } else if (i == R.id.btn_21) {
            if (progress2 == 100) {
                stopProgress2();
                progress2 = 0;
            }
            startProgress2();

        } else if (i == R.id.btn_22) {
            stopProgress2();

        } else if (i == R.id.btn_31) {
            if (progress3 == 100) {
                stopProgress3();
                progress3 = 0;
            }
            startProgress3();

        } else if (i == R.id.btn_32) {
            stopProgress3();

        }
    }



    private void initBarPercent() {
        bar_percent = (RingProgressBar) findViewById(R.id.bar_percent);
        //设置进度
        bar_percent.setProgress(0);
        //设置更新进度条颜色
        bar_percent.setDotColor(this.getResources().getColor(R.color.colorAccent));
        //设置未更新部分的进度条颜色
        bar_percent.setDotBgColor(this.getResources().getColor(R.color.blackText));
        //设置百分比文字颜色
        bar_percent.setPercentTextColor(this.getResources().getColor(R.color.blackText1));
        //设置百分比文字大小
        bar_percent.setPercentTextSize(ProgressBarUtils.dp2px(this,16.0f));
        //设置展示的类型
        bar_percent.setShowMode(ProgressBarUtils.RingShowMode.SHOW_MODE_PERCENT);
        //设置单位的文字内容
        bar_percent.setUnitText("%");
        //设置单位的文字大小
        bar_percent.setUnitTextSize(ProgressBarUtils.dp2px(this,16.0f));
        //设置单位的文字颜色
        bar_percent.setUnitTextColor(this.getResources().getColor(R.color.blackText1));
    }

    private void readyProgress1() {
        if (mTimer1 == null) {
            mTimer1 = new Timer();
        }
        if (mTimerTask1 == null) {
            mTimerTask1 = new TimerTask() {
                @Override
                public void run() {
                    if (!isProgressGoing1) {
                        return;
                    }
                    if (++progress1 >= max) {
                        progress1 = max;
                        bar_percent.setProgress(progress1);
                        stopProgress1();
                        return;
                    }
                    bar_percent.setProgress(progress1);
                }
            };
        }
    }

    private void startProgress1() {
        isProgressGoing1 = true;
        stopTimerTask1();
        readyProgress1();
        mTimer1.schedule(mTimerTask1, 1000, 100);
    }

    private void stopTimerTask1() {
        if (mTimerTask1 != null) {
            mTimerTask1.cancel();
        }
        if (mTimer1 != null) {
            mTimer1.cancel();
        }
        mTimerTask1 = null;
        mTimer1 = null;
    }

    private void stopProgress1() {
        isProgressGoing1 = false;
        stopTimerTask1();
    }





    private void initBarNull() {
        bar_null = findViewById(R.id.bar_null);
        bar_null.setProgress(89);
        bar_null.setPercentTextColor(this.getResources().getColor(R.color.redTab));
        bar_null.setUnitTextColor(this.getResources().getColor(R.color.redTab));
    }

    private void readyProgress2() {
        if (mTimer2 == null) {
            mTimer2 = new Timer();
        }
        if (mTimerTask2 == null) {
            mTimerTask2 = new TimerTask() {
                @Override
                public void run() {
                    if (!isProgressGoing2) {
                        return;
                    }
                    if (++progress2 >= max) {
                        progress2 = max;
                        bar_null.setProgress(progress2);
                        stopProgress2();
                        return;
                    }
                    bar_null.setProgress(progress2);
                }
            };
        }
    }

    private void startProgress2() {
        isProgressGoing2 = true;
        stopTimerTask2();
        readyProgress2();
        mTimer2.schedule(mTimerTask2, 1000, 100);
    }

    private void stopTimerTask2() {
        if (mTimerTask2 != null) {
            mTimerTask2.cancel();
        }
        if (mTimer2 != null) {
            mTimer2.cancel();
        }
        mTimerTask2 = null;
        mTimer2 = null;
    }

    private void stopProgress2() {
        isProgressGoing2 = false;
        stopTimerTask2();
    }




    private void initBarAll() {
        bar_all = findViewById(R.id.bar_all);
        bar_all.setProgressMax(max);
        bar_all.setPercentTextColor(this.getResources().getColor(R.color.colorPrimary));
        bar_all.setUnitTextColor(this.getResources().getColor(R.color.colorPrimary));
        bar_all.setPercentTextSize(ProgressBarUtils.dp2px(this,30.0f));
        bar_all.setUnitTextSize(ProgressBarUtils.dp2px(this,16.0f));
    }


    private void readyProgress3() {
        if (mTimer3 == null) {
            mTimer3 = new Timer();
        }
        if (mTimerTask3 == null) {
            mTimerTask3 = new TimerTask() {
                @Override
                public void run() {
                    if (!isProgressGoing3) {
                        return;
                    }
                    if (++progress3 >= max) {
                        progress3 = max;
                        bar_all.setProgress(progress3);
                        stopProgress3();
                        return;
                    }
                    bar_all.setProgress(progress3);
                }
            };
        }
    }

    private void startProgress3() {
        isProgressGoing3 = true;
        stopTimerTask3();
        readyProgress3();
        mTimer3.schedule(mTimerTask3, 1000, 100);
    }

    private void stopTimerTask3() {
        if (mTimerTask3 != null) {
            mTimerTask3.cancel();
        }
        if (mTimer3 != null) {
            mTimer3.cancel();
        }
        mTimerTask3 = null;
        mTimer3 = null;
    }

    private void stopProgress3() {
        isProgressGoing3 = false;
        stopTimerTask3();
    }


}

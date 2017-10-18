package com.ns.yc.lifehelper.ui.guide;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.api.ConstantImageApi;
import com.ns.yc.lifehelper.api.ConstantKeys;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.receiver.TimerReceiver;
import com.ns.yc.lifehelper.service.TimerService;
import com.ns.yc.lifehelper.ui.main.MainActivity;
import com.ns.yc.lifehelper.utils.DialogUtils;
import com.ns.yc.lifehelper.utils.ImageUtils;
import com.ns.yc.yccountdownviewlib.CountDownView;

import java.util.Calendar;
import java.util.Random;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/18
 * 描    述：启动页面
 * 修订历史：
 * ================================================
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_splash_ad)
    ImageView ivSplashAd;
    @Bind(R.id.tv_splash_skip_ad)
    TextView tvSplashSkipAd;
    @Bind(R.id.iv_splash_download)
    ImageView ivSplashDownload;
    @Bind(R.id.cdv_time)
    CountDownView cdvTime;

    @Override
    public int getContentView() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView() {
        initState();
        initImage();
        initConstantData();
        initCountDownView();
        initService();
        //initTimer();
        //initAlarmManager();
        //initAlarmManagerSecond();
    }


    @Override
    public void initListener() {
        //tvSplashSkipAd.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    private void initImage() {
        // 先显示默认图
        int i = new Random().nextInt(ConstantImageApi.SPALSH_URLS.length);
        ImageUtils.loadImgByPicassoError(this, ConstantImageApi.SPALSH_URLS[i], R.drawable.pic_page_background, ivSplashAd);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toMainActivity();
            }
        }, 3000);*/
    }

    private void initCountDownView() {
        cdvTime.setTime(5);
        cdvTime.start();
        cdvTime.setOnLoadingFinishListener(new CountDownView.OnLoadingFinishListener() {
            @Override
            public void finish() {
                toMainActivity();
            }
        });
        cdvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdvTime.stop();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_splash_skip_ad:
                toMainActivity();
                break;
        }
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();
    }


    private void initConstantData() {
        Constant.isLogin = SPUtils.getInstance(Constant.SP_NAME).getBoolean(ConstantKeys.IS_LOGIN);
    }

    /**
     * 开启一个服务，在服务中倒计时
     */
    private void initService() {
        Intent intent = new Intent(this, TimerService.class);
        startService(intent);
    }

    /**
     * 倒计时
     */
    private void initTimer() {
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                DialogUtils.showWindowToast("全局吐司");
                //IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity.
                //DialogUtils.showAllAlertDialog(GuideActivity.this);
                DialogUtils.showWindowDialog();
            }
        }.start();
    }


    /**
     * 在指定时长后执行某项操作
     */
    private void initAlarmManager() {
        //操作：发送一个广播，广播接收后Toast提示定时操作完成
        Intent intent = new Intent(this, TimerReceiver.class);
        intent.setAction("short");
        PendingIntent sender= PendingIntent.getBroadcast(this, 0, intent, 0);

        //设定一个n秒后的时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 30);

        AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        //或者以下面方式简化
        //alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5*1000, sender);
    }


    /**
     * 周期性的执行某项操作
     */
    private void initAlarmManagerSecond() {
        Intent intent =new Intent(this, TimerReceiver.class);
        intent.setAction("repeating");
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        //开始时间
        long firstTime = SystemClock.elapsedRealtime();

        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        //5秒一个周期，不停的发送广播
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 5*1000, sender);
    }


}

package com.yc.other.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;


import com.yc.businessinterface.IAnimServiceProvider;
import com.yc.spi.loader.ServiceLoader;
import com.yc.widget.blurview.RealTimeBlurView;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.other.R;

public class TestActivity extends BaseActivity implements View.OnClickListener {

    private final IAnimServiceProvider mDelegate = ServiceLoader.load(IAnimServiceProvider.class).get();


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
            mDelegate.setAnimTime(10);
            startActivity(new Intent(this, BannerViewActivity.class));
        } else if (i == R.id.tv_3) {
            mDelegate.getAnimTime();
            startActivity(new Intent(this, ImageGalleryActivity.class));
        } else if (i == R.id.tv_4) {
            mDelegate.setStartAnim();
            startActivity(new Intent(this, ProgressThirdActivity.class));
        } else if (i == R.id.tv_5) {
        } else if (i == R.id.tv_6){
            startActivity(new Intent(this, TestFirstActivity.class));
        } else if (i == R.id.tv_7){
        } else if (i == R.id.tv_8){
            startActivity(new Intent(this, MixtureTextViewActivity.class));
        }else if (i == R.id.tv_9){
            startActivity(new Intent(this, CloneAbleActivity.class));
        }else if (i == R.id.tv_10){
            startShowDialog();
        } else if (i == R.id.tv_12){
            startActivity(new Intent(this, SerialTaskActivity.class));
        }
    }


    private void startShowDialog() {
        View popMenuView = this.getLayoutInflater().inflate(R.layout.view_real_blur_view, null);
        RealTimeBlurView blur_view = (RealTimeBlurView) popMenuView.findViewById(R.id.blur_view);
        float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, this.getResources().getDisplayMetrics());
        blur_view.setBlurRadius(v);
        final PopupWindow popMenu = new PopupWindow(popMenuView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popMenu.setClippingEnabled(false);
        popMenu.setFocusable(true);         //点击其他地方关闭
        popMenu.showAtLocation(popMenuView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }



}

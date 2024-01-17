package com.yc.widgetbusiness.blur;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.blurview.view.RealTimeBlurView;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.widgetbusiness.R;
import com.yc.widgetbusiness.banner.MeBannerActivity;


public class BlurActivity extends AppCompatActivity implements View.OnClickListener {

    private RoundTextView tvView1;
    private RoundTextView tvView2;
    private RoundTextView tvView3;
    private RoundTextView tvView4;
    private RoundTextView tvView5;
    private RoundTextView tvView6;
    private RoundTextView tvView7;
    private RoundTextView tvView8;
    private RoundTextView tvView9;
    private RoundTextView tvView10;
    private RoundTextView tvView11;
    private RoundTextView tvView12;
    private ImageView ivImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_view);
        initView();
        initListener();
        initData();
    }

    public void initView() {
        tvView1 = findViewById(R.id.tv_view_1);
        tvView2 = findViewById(R.id.tv_view_2);
        tvView3 = findViewById(R.id.tv_view_3);
        tvView4 = findViewById(R.id.tv_view_4);
        tvView5 = findViewById(R.id.tv_view_5);
        tvView6 = findViewById(R.id.tv_view_6);
        tvView7 = findViewById(R.id.tv_view_7);
        tvView8 = findViewById(R.id.tv_view_8);
        tvView9 = findViewById(R.id.tv_view_9);
        tvView10 = findViewById(R.id.tv_view_10);
        tvView11 = findViewById(R.id.tv_view_11);
        tvView12 = findViewById(R.id.tv_view_12);
        ivImageView = findViewById(R.id.iv_image_view);
    }

    public void initListener() {
        tvView1.setOnClickListener(this);
        tvView2.setOnClickListener(this);
        tvView3.setOnClickListener(this);
        tvView4.setOnClickListener(this);
        tvView5.setOnClickListener(this);
        tvView6.setOnClickListener(this);
        tvView7.setOnClickListener(this);
    }

    public void initData() {
        tvView1.setText("1.自定义高斯模糊View");
        tvView2.setText("2.利用算法让bitmap高斯模糊");
    }

    @Override
    public void onClick(View v) {
        if (v == tvView1){
            startShowDialog();
        } else if (v == tvView2){
            startActivity(new Intent(this, MeBannerActivity.class));
        }
    }

    private void startShowDialog() {
        View popMenuView = this.getLayoutInflater().inflate(R.layout.view_real_blur_view, null);
        RealTimeBlurView blurView = popMenuView.findViewById(R.id.blur_view);
        blurView.setBlurRadius(15);
        final PopupWindow popMenu = new PopupWindow(popMenuView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popMenu.setClippingEnabled(false);
        popMenu.setFocusable(true);
        popMenu.showAtLocation(popMenuView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}

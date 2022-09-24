package com.yc.widgetbusiness.floatpage;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.roundcorner.view.RoundTextView;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.toastutils.ToastUtils;
import com.yc.window.FloatWindow;
import com.yc.window.draggable.MovingDraggable;
import com.yc.toolutils.click.PerfectClickListener;
import com.yc.widgetbusiness.R;
import com.yc.window.draggable.SpringDraggable;
import com.yc.window.permission.FloatWindowUtils;
import com.yc.window.permission.PermissionActivity;

public class FloatActivity extends AppCompatActivity {

    private RoundTextView tvWidgetFloat1;
    private RoundTextView tvWidgetFloat2;
    private RoundTextView tvWidgetFloat3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_float);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        initView();
        initListener();
    }

    private void initView() {
        tvWidgetFloat1 = findViewById(R.id.tv_widget_float1);
        tvWidgetFloat2 = findViewById(R.id.tv_widget_float2);
        tvWidgetFloat3 = findViewById(R.id.tv_widget_float3);
    }

    public void initListener() {
        tvWidgetFloat1.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showFloat1();
            }
        });
        tvWidgetFloat2.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (FloatWindowUtils.hasPermission(FloatActivity.this)) {
                        showFloat2();
                    } else {
                        PermissionActivity.request(FloatActivity.this, new PermissionActivity.PermissionListener() {
                            @Override
                            public void onSuccess() {
                                showFloat2();
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                    }
                }

            }
        });
        tvWidgetFloat3.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

            }
        });
    }

    private void showFloat2() {
        // 传入 Application 表示这个是一个全局的 Toast
        new FloatWindow<>(this.getApplication())
                .setContentView(R.layout.float_window_view)
                .setGravity(Gravity.END | Gravity.BOTTOM)
                .setYOffset(200)
                // 设置指定的拖拽规则
                .setDraggable(new SpringDraggable())
                .setOnClickListener(R.id.icon, new FloatWindow.OnClickListener<ImageView>() {
                    @Override
                    public void onClick(FloatWindow<?> toast, ImageView view) {
                        ToastUtils.showRoundRectToast("我被点击了");
                        //toast.cancel();
                        // 点击后跳转到拨打电话界面
                        // Intent intent = new Intent(Intent.ACTION_DIAL);
                        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // toast.startActivity(intent);
                        // 安卓 10 在后台跳转 Activity 需要额外适配
                        // https://developer.android.google.cn/about/versions/10/privacy/changes#background-activity-starts
                    }
                })
                .show();
    }

    private void showFloat1() {
        new FloatWindow<>(this)
                .setContentView(R.layout.float_window_view)
                // 设置成可拖拽的
                .setDraggable(new MovingDraggable())
                .setOnClickListener(R.id.icon, new FloatWindow.OnClickListener<View>() {
                    @Override
                    public void onClick(FloatWindow<?> toast, View view) {
                        toast.cancel();
                    }
                })
                .show();
    }

}

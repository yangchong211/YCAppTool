package com.yc.widgetbusiness.floatpage;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.roundcorner.view.RoundTextView;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.toastutils.ToastUtils;
import com.yc.window.FloatWindow;
import com.yc.window.draggable.MovingTouchListener;
import com.yc.toolutils.click.PerfectClickListener;
import com.yc.widgetbusiness.R;
import com.yc.window.draggable.SpringTouchListener;
import com.yc.window.inter.IClickListener;
import com.yc.window.permission.FloatWindowUtils;
import com.yc.window.permission.PermissionActivity;

public class FloatActivity extends AppCompatActivity {

    private RoundTextView tvWidgetFloat11;
    private RoundTextView tvWidgetFloat12;
    private RoundTextView tvWidgetFloat2;
    private RoundTextView tvWidgetFloat3;
    private FloatWindow floatWindow2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_float);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        initView();
        initListener();
    }

    private void initView() {


        tvWidgetFloat11 = findViewById(R.id.tv_widget_float1_1);
        tvWidgetFloat12 = findViewById(R.id.tv_widget_float1_2);
        tvWidgetFloat2 = findViewById(R.id.tv_widget_float2);
        tvWidgetFloat3 = findViewById(R.id.tv_widget_float3);

    }

    public void initListener() {
        tvWidgetFloat11.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showFloat1();
            }
        });
        tvWidgetFloat12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatWindow1 !=null){
                    floatWindow1.dismiss();
                }
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
                if (floatWindow2!=null){
                    floatWindow2.dismiss();
                }
            }
        });
    }

    private void showFloat2() {
        // 传入 Application 表示这个是一个全局的 Toast
        if (floatWindow2==null){
            floatWindow2 = new FloatWindow(this.getApplication())
                    .setContentView(R.layout.float_window_view)
                    .setSize(200, 400)
                    .setGravity(Gravity.END | Gravity.BOTTOM, 0, 200)
                    // 设置指定的拖拽规则
                    .setDraggable(new SpringTouchListener())
                    .setOnClickListener(R.id.icon, new IClickListener() {
                        @Override
                        public void onClick(FloatWindow toast, View view) {
                            ToastUtils.showRoundRectToast("我被点击了");
                            //toast.cancel();
                            // 点击后跳转到拨打电话界面
                            // Intent intent = new Intent(Intent.ACTION_DIAL);
                            // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            // toast.startActivity(intent);
                            // 安卓 10 在后台跳转 Activity 需要额外适配
                            // https://developer.android.google.cn/about/versions/10/privacy/changes#background-activity-starts
                        }
                    });
        }
        floatWindow2.show();
    }

    FloatWindow floatWindow1;
    private void showFloat1() {
        if (floatWindow1 == null){
            floatWindow1 = new FloatWindow(this)
                    .setContentView(R.layout.float_window_view)
                    // 设置成可拖拽的
                    .setDraggable(new MovingTouchListener())
                    .setOnClickListener(R.id.icon, new IClickListener() {
                        @Override
                        public void onClick(FloatWindow floatWindow, View view) {
                            floatWindow.dismiss();
                        }
                    });
        }
        floatWindow1.show();
    }

}
